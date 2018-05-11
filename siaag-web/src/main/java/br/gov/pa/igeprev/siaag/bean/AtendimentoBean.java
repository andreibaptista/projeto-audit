package br.gov.pa.igeprev.siaag.bean;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.gov.pa.igeprev.siaag.enumeration.*;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.*;
import br.gov.pa.igeprev.siaag.model.view.VwDatasDisponiveis;
import br.gov.pa.igeprev.siaag.model.view.VwHorarioAgenda;
import br.gov.pa.igeprev.siaag.repository.ArquivoRepository;
import br.gov.pa.igeprev.siaag.repository.SituacaoAtendimentoRepository;
import br.gov.pa.igeprev.siaag.repository.VwDatasDisponiveisRepository;
import br.gov.pa.igeprev.siaag.repository.VwHorarioAgendaRepository;
import br.gov.pa.igeprev.siaag.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
@Controller
public class AtendimentoBean extends ManagerBeanDefault {

    private static final long serialVersionUID = 1L;

    @Autowired
    private VwHorarioAgendaRepository vwHorarioAgendaRepository;
    @Autowired
    private VwDatasDisponiveisRepository vwDatasDisponiveisRepository;
    @Autowired
    private AtendimentoService atendimentoService;
    @Autowired
    private TipoBeneficiarioService tipoBeneficiarioService;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private PontoAtendimentoService pontoAtendimentoService;
    @Autowired
    private TipoAtendimentoService tipoAtendimentoService;
    @Autowired
    private HorarioService horarioService;
    @Autowired
    private SituacaoAtendimentoRepository situacaoRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;

    private Atendimento atendimento = new Atendimento();

    private Collection<Atendimento> atendimentos = new ArrayList<>();
    private Collection<TipoBeneficiario> tipoBeneficiarios = new ArrayList<>();
    private List<PontoAtendimento> pontoAtendimentos = new ArrayList<>();
    private List<TipoAtendimento> tipoAtendimentos = new ArrayList<>();
    private List<VwDatasDisponiveis> datasDisponiveis = new ArrayList<>();
    private List<Horario> horariosDisponiveisFiltrado = new ArrayList<>();
    private List<Horario> horariosDisponiveisMacro = new ArrayList<>();
    private List<Horario> horarios = new ArrayList<>();
    private Collection<SituacaoAtendimento> situacaoAtendimentoInativas = new ArrayList<>();

    private Horario horarioSelecionado = new Horario();

    private VwDatasDisponiveis dataSelecionada;

    private Boolean pesquisouCpf = false;

    private Boolean confirmouDados = false;

    private String cpfPesq = "";

    @Override
    public void inicializacao() throws ServiceException {
        atendimento = new Atendimento();
        tipoBeneficiarios = tipoBeneficiarioService.findAll();
        pontoAtendimentos = new ArrayList<>(pontoAtendimentoService.findAll());
        tipoAtendimentos = new ArrayList<>(tipoAtendimentoService.findAll());
        horarios = new ArrayList<>(horarioService.findByAtivo());
        situacaoAtendimentoInativas.add(new SituacaoAtendimento(SituacaoAtendimentoEnum.CANCELADO.getId()));
        situacaoAtendimentoInativas.add(new SituacaoAtendimento(SituacaoAtendimentoEnum.EXPIRADO.getId()));
        buscarListaEntidades();
    }

    public void pesquisaAtendimentos() {
        try {
            if (cpfPesq.length() == 0 || cpfPesq.length() == 11) {
                if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.RECEPCIONISTA.getId()) ||
                        segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.ADMINISTRADOR.getId()) ||
                        segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.TECNICO.getId())) {
                    if (cpfPesq.isEmpty())
                        atendimentos = atendimentoService.findAllByAtivoOrderByDataHorarioInicio(Boolean.TRUE);
                    else atendimentos = atendimentoService.findByBeneficiarioCpf(cpfPesq);
                } else if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.ATENDENTE.getId())) {
                    if (cpfPesq.isEmpty())
                        atendimentos = atendimentoService.findByAtendenteId(segurancaBean.getUsuarioLogado().getPessoa().getId());
                    else
                        atendimentos = atendimentoService.findByAtendenteIdAAndBeneficiarioCpf(segurancaBean.getUsuarioLogado().getPessoa().getId(), cpfPesq);
                } else if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.CLIENTE.getId())) {
                    if (cpfPesq.isEmpty())
                        atendimentos = atendimentoService.findByBeneficiarioId(segurancaBean.getUsuarioLogado().getPessoa().getId());
                    else
                        atendimentos = atendimentoService.findByUsuarioCadastrouIdAndBeneficiarioCpf(segurancaBean.getUsuarioLogado().getPessoa().getId(), cpfPesq);
                }
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    protected void novoRegistro() {
        atendimento = new Atendimento();
        confirmouDados = false;
        atendimento.setFormaAtendimento(FormaAtendimentoEnum.AGENDADO);
        if (pontoAtendimentos != null && pontoAtendimentos.size() == 1)
            atendimento.setPontoAtendimento(pontoAtendimentos.get(0));
        if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.CLIENTE.getId())) {
            atendimento.setBeneficiario(segurancaBean.getUsuarioLogado().getPessoa());
        } else {
            horarioSelecionado = new Horario();
        }
        dataSelecionada = new VwDatasDisponiveis();
        pesquisaCpf();
        carregaAgendasDisponiveis();
    }

    @Override
    protected void editarRegistro(Object obj) {
        atendimento = (Atendimento) obj;
        carregaAgendasDisponiveis();
        confirmouDados = true;
    }

    @Override
    protected void visualizarRegistro(Object obj) {
        atendimento = (Atendimento) obj;
        try {
            atendimento.setArquivos(new ArrayList<>(arquivoRepository.findByAtendimentoId(atendimento.getId())));
        } catch (Exception e) {
            showErrorMessage();
        }
    }

    @Override
    protected void cancelarOperacao() throws ServiceException {
        buscarListaEntidades();
    }

    @Override
    protected void buscarListaEntidades() throws ServiceException {
        if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.RECEPCIONISTA.getId()) ||
                segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.ADMINISTRADOR.getId()) ||
                segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.TECNICO.getId())) {
            atendimentos = atendimentoService.findAllByAtivoOrderByDataHorarioInicio(Boolean.TRUE);
        } else if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.ATENDENTE.getId())) {
            atendimentos = atendimentoService.findByAtendenteId(segurancaBean.getUsuarioLogado().getPessoa().getId());
            atendimentos.addAll(atendimentoService.findByBeneficiarioId(segurancaBean.getUsuarioLogado().getPessoa().getId()));
        } else if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.CLIENTE.getId())) {
            atendimentos = atendimentoService.findByBeneficiarioId(segurancaBean.getUsuarioLogado().getPessoa().getId());
        }
    }

    public void confirmarDados() {
        confirmouDados = true;
    }

    @Override
    protected void remover() throws ServiceException {
        atendimentoService.atualizarSituacao(situacaoRepository.findOne(SituacaoAtendimentoEnum.CANCELADO.getId()), atendimento.getId());
    }

    @Override
    protected void salvar() throws ServiceException {
        Atendimento atendimentoExistente = atendimentoService.findByAtivoAndBeneficiarioIdAndTipoAtendimentoIdAndData(true, atendimento.getBeneficiario().getId(), atendimento.getTipoAtendimento().getId(), dataSelecionada.getData());
/*
        List<Usuario> atendentes = new ArrayList<>(vwHorarioAgendaRepository.findAtendentesByDataAndHorario(dataSelecionada.getData(), horarioSelecionado.getId())); //TODO:Excluir esse metodo.
*/
        if (atendimentoExistente == null) {
            verificaCarregaHorariosDisponiveis();
            if (!horariosDisponiveisMacro.contains(horarioSelecionado)) {
                horarioSelecionado = verificaBuscaOutroHorario(horariosDisponiveisMacro, horarioSelecionado);
            }
            if (horarioSelecionado != null) {
                atendimento.setData(dataSelecionada.getData());
                atendimento.setHorarioInicio(horarioSelecionado);
                atendimento.setAtendente(horarioSelecionado.getAtendente());
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(atendimento.getHorarioInicio().getHorario());
                gc.add(Calendar.MINUTE, atendimento.getTipoAtendimento().getTempoAtendimento());
                Horario horarioFim = horarioService.findByHorario(gc.getTime());
                atendimento.setHorarioFim(horarioFim);
                if (isEditando()) {
                    atendimento.setSituacaoAtendimento(situacaoRepository.findOne(SituacaoAtendimentoEnum.REAGENDADO.getId()));
                } else {
                    atendimento.setSituacaoAtendimento(situacaoRepository.findOne(SituacaoAtendimentoEnum.ATIVO.getId()));
                }
                atendimento.setPrioridadeContato(PrioridadeContatoEnum.EMAIL);
                atendimento.setRequerente(atendimento.getBeneficiario());
                atendimento.setTipoRequerente(new TipoRequerente(TipoRequerenteEnum.PROPRIO.getId()));
                atendimento.setUsuarioCadastrou(segurancaBean.getUsuarioLogado());
                atendimento.setResponsavel(segurancaBean.getUsuarioLogado().getPessoa());
                atendimento = atendimentoService.salvar(atendimento);
                setRedirecionaTela(true);
                showSuccessMessage(getMessage("mensagemSucessoAtendimentoCadastrado"));
            } else {
                showErrorMessage(getMessage("horarioNaoDisponivel"));
                setErroValidacao(true);
            }
        } else {
            showErrorMessage(getMessage("mensagemErroAtendimentoTipoAtendimentoExistente"));
            setErroValidacao(true);
        }
    }

    private Horario verificaBuscaOutroHorario(List<Horario> horariosDisponiveisMacro, Horario horarioSelecionado) {
        for (Horario horarioDisp : horariosDisponiveisMacro) {
            if (horarioDisp.getId().equals(horarioSelecionado.getId())) {
                return horarioDisp;
            }
        }
        return null;
    }

    public void pesquisaCpf() { //TODO: Mudar esse metodo par ao bean.
        String cpf = "";
        try {
            CPFValidator cpfValidator = new CPFValidator();
            cpf = atendimento.getBeneficiario().getCpf();
            if (cpf != null && !cpf.isEmpty() && cpf.length() == 11) {
                atendimento.setBeneficiario(new Pessoa());
                atendimento.getBeneficiario().setCpf(cpf);
                Pessoa pessoa = buscaPessoa(atendimento.getBeneficiario());
                if (pessoa != null) atendimento.setBeneficiario(pessoa);
                else {
                    cpfValidator.assertValid(cpf);
                    atendimento.setBeneficiario(new Pessoa());
                    pesquisouCpf = true;
                    return;
                }
                pesquisouCpf = true;
            }
        } catch (InvalidStateException e) {
            pesquisouCpf = false;
            atendimento.setBeneficiario(new Pessoa());
            atendimento.getBeneficiario().setCpf(cpf);
            showErrorMessage(getMessage("CPFInvalido"));
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public void carregaAgendasDisponiveis() {
        try {
            dataSelecionada = null;
            horariosDisponiveisFiltrado = new ArrayList<>();
            if (atendimento.getFormaAtendimento() != null) {
                datasDisponiveis = new ArrayList<>(vwDatasDisponiveisRepository.findByFormaAtendimento(atendimento.getFormaAtendimento()));
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public void verificaCarregaHorariosDisponiveis() {
        horariosDisponiveisFiltrado = new ArrayList<>();
        horariosDisponiveisMacro = new ArrayList<>();
        atendimento.setHorarioInicio(new Horario());
        if (dataSelecionada != null && atendimento.getFormaAtendimento() != null) {
            try {
                List<VwHorarioAgenda> vwHorarioAgendas = new ArrayList<>(vwHorarioAgendaRepository.findByData(dataSelecionada.getData()));
                List<Horario> horarios = new ArrayList<>();
                List<Horario> horariosBloqueio = new ArrayList<>();

                for (VwHorarioAgenda vwHorarioAgenda : vwHorarioAgendas) {
                    horarios.addAll(horariosEntreIntervalo(vwHorarioAgenda.getHorarioInicioAgendaItem(), vwHorarioAgenda.getHorarioFimAgendaItem(), vwHorarioAgenda.getAtendente().getPessoa()));
                    if (vwHorarioAgenda.getHorarioInicioBloqueio() != null && vwHorarioAgenda.getHorarioFimBloqueio() != null) {
                        horariosBloqueio.addAll(new ArrayList<>(horariosEntreIntervalo(vwHorarioAgenda.getHorarioInicioBloqueio(), vwHorarioAgenda.getHorarioFimBloqueio(), vwHorarioAgenda.getAtendente().getPessoa())));
                    }
                }

                //Adiciona os horarios que já foram marcado atendimentos aos de bloqueio.
                List<Atendimento> atendimentos = new ArrayList<>(atendimentoService.findByDataAndFormaAtendimentoAndSituacaoAtendimentoNotIn(dataSelecionada.getData(), FormaAtendimentoEnum.AGENDADO, situacaoAtendimentoInativas));
                for (Atendimento atendimento : atendimentos) {
                    List<Horario> horariosAtendimento = horariosEntreIntervalo(atendimento.getHorarioInicio(), atendimento.getHorarioFim(), atendimento.getAtendente());
                    for (Horario horarioAtend : horariosAtendimento) {
                        Horario horario = new Horario(horarioAtend.getId(), horarioAtend.getHorario(), horarioAtend.getAtendente(), true);
                        if (horariosAtendimento.size() - 1 == horariosAtendimento.indexOf(horarioAtend))
                            break; //Ignora o ultimo horario.
                        horariosBloqueio.add(horario);
                    }
                }
                for (Horario horario : horariosBloqueio) {
                    horarios.remove(horario);
                }
                horariosDisponiveisMacro.addAll(horarios);
                horariosDisponiveisFiltrado = new ArrayList<>(horariosNaoRepetidos(horarios));
                Collections.sort(horariosDisponiveisFiltrado);
            } catch (Exception e) {
                showErrorMessage();
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
    }

    private List<Horario> horariosNaoRepetidos(List<Horario> horarios) {

        List<Horario> naoRepetido = new ArrayList<>();

        for (Horario horario : horarios) {
            boolean isFound = false;
            for (Horario e : naoRepetido) {
                if (e.getId().equals(horario.getId())) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) naoRepetido.add(horario);
        }
        return naoRepetido;
    }

    private List<Horario> horariosEntreIntervalo(Horario inicio, Horario fim, Pessoa atendente) {
        List<Horario> lista = new ArrayList<>();
        List<Integer> listaInteiros = new ArrayList<>();
        try {
            if (!fim.getAtivo()) fim = horarioService.findUltimoHorarioAtivo();
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        for (int i = inicio.getId() - 1; i < fim.getId(); i++) {
            listaInteiros.add(i);
        }
        Arrays.sort(listaInteiros.toArray());
        for (Integer id : listaInteiros) {
            Horario horario = new Horario(horarios.get(id).getId(), horarios.get(id).getHorario(), atendente, true);
            lista.add(horario);
        }
        return lista;
    }

    public void downloadArquivo(Arquivo arquivo) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=\\" + arquivo.getDescricao().trim());
        ServletOutputStream streamDeSaida = response.getOutputStream();
        streamDeSaida.write(arquivo.getArquivo());
        streamDeSaida.close();

        facesContext.responseComplete();
    }

    public void voltarConfirmarDados() {
        confirmouDados = false;
    }

    @Override
    public String getNomeFormulario() {
        return "protected/form-atendimento.xhtml";
    }

    @Override
    protected String getMsgTraceAlteracao() {
        return "Alterou o objeto " + atendimento.getClass().getName() + " com id " + atendimento.getId();
    }

    @Override
    protected String getMsgTraceInclusao() {
        return "Cadastrou o objeto " + atendimento.getClass().getName() + " com id " + atendimento.getId();
    }

    @Override
    protected String getMsgTraceExclusao() {
        return "Excluiu o objeto " + atendimento.getClass().getName() + " com id " + atendimento.getId();
    }

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }

    public FormaAtendimentoEnum[] getFormaAtendimentos() {
        return FormaAtendimentoEnum.values();
    }

    public Collection<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    public Collection<TipoBeneficiario> getTipoBeneficiarios() {
        return tipoBeneficiarios;
    }

    public Collection<PontoAtendimento> getPontoAtendimentos() {
        return pontoAtendimentos;
    }

    public Collection<TipoAtendimento> getTipoAtendimentos() {
        return tipoAtendimentos;
    }

    public Boolean getPesquisouCpf() {
        return pesquisouCpf;
    }

    public Boolean getConfirmouDados() {
        return confirmouDados;
    }

    public void setDataSelecionada(VwDatasDisponiveis dataSelecionada) {
        this.dataSelecionada = dataSelecionada;
    }

    public VwDatasDisponiveis getDataSelecionada() {
        return dataSelecionada;
    }

    public List<Horario> getHorariosDisponiveisFiltrado() {
        return horariosDisponiveisFiltrado;
    }

    public String[] getSpecialDays() {
        List<VwDatasDisponiveis> datas = new ArrayList<>(datasDisponiveis);
        if (datas != null && !datas.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String[] result = new String[3];
            if (datas != null) {
                result = new String[datas.size()];

                for (int i = 0; i < datas.size(); i++) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(datas.get(i).getData());
                    result[i] = String.format("'%s'", sdf.format(cal.getTime()));
                }
            }
            return result;
        }
        return new String[0];
    }

    public List<VwDatasDisponiveis> getDatasDisponiveis() {
        return datasDisponiveis;
    }

    public void setDatasDisponiveis(List<VwDatasDisponiveis> datasDisponiveis) {
        this.datasDisponiveis = datasDisponiveis;
    }

    public Horario getHorarioSelecionado() {
        return horarioSelecionado;
    }

    public void setHorarioSelecionado(Horario horarioSelecionado) {
        this.horarioSelecionado = horarioSelecionado;
    }

    public String getCpfPesq() {
        return cpfPesq;
    }

    public void setCpfPesq(String cpfPesq) {
        this.cpfPesq = cpfPesq;
    }

}
