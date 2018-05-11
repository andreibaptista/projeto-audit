package br.gov.pa.igeprev.siaag.bean;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.gov.pa.igeprev.siaag.enumeration.*;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.*;
import br.gov.pa.igeprev.siaag.model.external.VwCadastroSiaag;
import br.gov.pa.igeprev.siaag.model.view.VwPainelAtendimento;
import br.gov.pa.igeprev.siaag.repository.ArquivoRepository;
import br.gov.pa.igeprev.siaag.repository.SituacaoAtendimentoRepository;
import br.gov.pa.igeprev.siaag.repository.VwPainelAtendimentoRepository;
import br.gov.pa.igeprev.siaag.service.*;
import br.gov.pa.igeprev.siaag.service.external.VwCadastroSiaagService;
import br.gov.pa.igeprev.siaag.utils.DataUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@ManagedBean
@ViewScoped
@Controller
public class PainelAtendimentoBean extends ManagerBeanDefault {

    private static final long serialVersionUID = 1L;

    @Autowired
    AtendimentoService atendimentoService;

    @Autowired
    VwPainelAtendimentoRepository painelAtendimentoRepository;
    @Autowired
    TipoAtendimentoService tipoAtendimentoService;
    @Autowired
    SituacaoAtendimentoRepository situacaoAtendimentoRepository;
    @Autowired
    TipoBeneficiarioService tipoBeneficiarioService;
    @Autowired
    PessoaService pessoaService;
    @Autowired
    AgendaService agendaService;
    @Autowired
    SituacaoAtendimentoRepository situacaoRepository;
    @Autowired
    VwCadastroSiaagService vwCadastroSiaagService;
    @Autowired
    ArquivoRepository arquivoRepository;

    private List<VwPainelAtendimento> atendimentosAgendados = new ArrayList<>();
    private List<VwPainelAtendimento> atendimentosNaoAgendados = new ArrayList<>();

    private List<TipoAtendimento> tipoAtendimentos = new ArrayList<>();
    private Collection<TipoBeneficiario> tipoBeneficiarios = new ArrayList<>();

    private Atendimento atendimento = new Atendimento();
    private Arquivo arquivo = new Arquivo();

    private Boolean cadastrandoNaoAgendado = false;
    private Boolean visualizandoNaoAgendado = false;
    private Boolean proximoNaoAgendado = false;
    private Boolean proximoAgendado = false;
    private Boolean atendeAtendimentoNaoAgendado = false;

    @Override
    public void inicializacao() throws ServiceException {
        buscarListaEntidades();
        tipoAtendimentos = new ArrayList<>(tipoAtendimentoService.findAll());
        tipoBeneficiarios = tipoBeneficiarioService.findAll();
        cadastrandoNaoAgendado = false;
        Collection<Agenda> agendasNaoAgendadas = agendaService.findByDataAndAtendimentoNaoAgendadoAndUsuarioId(DataUtils.SomenteData(new Date()), true, segurancaBean.getUsuarioLogado().getId());
        if (agendasNaoAgendadas.size() > 0) {
            atendeAtendimentoNaoAgendado = true;
        } else {
            atendeAtendimentoNaoAgendado = false;
        }
    }

    @Override
    protected void novoRegistro() {
        atendimento = new Atendimento();
    }

    @Override
    protected void editarRegistro(Object obj) throws ServiceException {
        VwPainelAtendimento vwPainelAtendimento = (VwPainelAtendimento) obj;
        atendimento = atendimentoService.findById(vwPainelAtendimento.getId());

        cadastrandoNaoAgendado = false;
        visualizandoNaoAgendado = false;
        proximoNaoAgendado = false;
        proximoAgendado = false;
        setPesquisouCpf(false);
    }

    @Override
    protected void visualizarRegistro(Object obj) throws ServiceException {
        VwPainelAtendimento vwPainelAtendimento = (VwPainelAtendimento) obj;
        atendimento = atendimentoService.findById(vwPainelAtendimento.getId());
        try {
            atendimento.setArquivos(new ArrayList<>(arquivoRepository.findByAtendimentoId(atendimento.getId())));
        } catch (Exception e) {
            showErrorMessage();
        }
        cadastrandoNaoAgendado = false;
        visualizandoNaoAgendado = false;
        proximoNaoAgendado = false;
        proximoAgendado = false;
        setPesquisouCpf(false);
    }

    @Override
    protected void cancelarOperacao() throws ServiceException {
        buscarListaEntidades();
        cadastrandoNaoAgendado = false;
        visualizandoNaoAgendado = false;
        proximoNaoAgendado = false;
        proximoAgendado = false;
        setPesquisouCpf(false);
    }

    public void novoNaoAgendado() {
        atendimento = new Atendimento();
        cadastrandoNaoAgendado = true;
        visualizandoNaoAgendado = false;
        proximoNaoAgendado = false;
        proximoAgendado = false;
        setPesquisouCpf(false);
        setRedirecionaTela(false);
        atendimento.setPontoAtendimento(segurancaBean.getUsuarioLogado().getPontoAtendimentoSetor().getPontoAtendimento());
        atendimento.setSituacaoAtendimento(situacaoAtendimentoRepository.findOne(SituacaoAtendimentoEnum.ATIVO.getId()));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Belem"));
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 3);
        atendimento.setDataNaoAgendado(cal.getTime());
        try {
            atendimento.setTipoBeneficiario(tipoBeneficiarioService.findById(TipoBeneficiarioEnum.PUBLICO_GERAL.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    protected void buscarListaEntidades() throws ServiceException {
        if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.RECEPCIONISTA.getId()) ||
                segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.ADMINISTRADOR.getId())) {
            atendimentosAgendados = new ArrayList<>(painelAtendimentoRepository.findByFormaAtendimento(FormaAtendimentoEnum.AGENDADO));
            atendimentosNaoAgendados = new ArrayList<>(painelAtendimentoRepository.findByFormaAtendimento(FormaAtendimentoEnum.NAO_AGENDADO));
        } else if (segurancaBean.getUsuarioLogado().getPerfil().getId().equals(PerfilEnum.ATENDENTE.getId())) {
            atendimentosAgendados = new ArrayList<>(painelAtendimentoRepository.findByAtendenteIdAndFormaAtendimento(segurancaBean.getUsuarioLogado().getPessoa().getId(), FormaAtendimentoEnum.AGENDADO));
            atendimentosNaoAgendados = new ArrayList<>(painelAtendimentoRepository.findByAtendenteIdAndFormaAtendimento(segurancaBean.getUsuarioLogado().getPessoa().getId(), FormaAtendimentoEnum.NAO_AGENDADO));
        }
    }

    @Override
    protected void remover() throws ServiceException {

    }

    @Override
    protected void salvar() throws ServiceException {
        if (atendimento.getId() == null) {
            atendimento.setAtendente(segurancaBean.getUsuarioLogado().getPessoa());
            atendimento.setRequerente(atendimento.getBeneficiario());
            atendimento.setPontoAtendimento(segurancaBean.getUsuarioLogado().getPontoAtendimentoSetor().getPontoAtendimento());
            Calendar cal = Calendar.getInstance();
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
            atendimento.setData(cal.getTime());
            atendimento.setHorarioInicio(null);
            atendimento.setHorarioFim(null);
            atendimento.setFormaAtendimento(FormaAtendimentoEnum.NAO_AGENDADO);
            atendimento.setTipoRequerente(new TipoRequerente(TipoRequerenteEnum.PROPRIO.getId()));
            atendimento.setUsuarioCadastrou(segurancaBean.getUsuarioLogado());
        } else {
            proximoAgendado = false;
        }
        atendimento.setSituacaoAtendimento(situacaoRepository.findOne(SituacaoAtendimentoEnum.ATENDIDO.getId()));
        atendimento.setResponsavel(segurancaBean.getUsuarioLogado().getPessoa());
        atendimento = atendimentoService.salvar(atendimento);
        showSuccessMessage(getMessage("mensagemSucessoCadastroAtendimento"));
        setRedirecionaTela(true);
        cadastrandoNaoAgendado = false;
        visualizandoNaoAgendado = false;
        proximoNaoAgendado = false;
        proximoAgendado = false;
        setPesquisouCpf(false);
        setRedirecionaTela(false);
        setOperacaoListando();
        buscarListaEntidades();
    }

    public void expirar() {
        try {
            atendimentoService.atualizarSituacao(situacaoAtendimentoRepository.findOne(SituacaoAtendimentoEnum.EXPIRADO.getId()), atendimento.getId());
            showSuccessMessage(getMessage("mensagemSucessoAtendimentoExpirado"));
            cadastrandoNaoAgendado = false;
            visualizandoNaoAgendado = false;
            proximoNaoAgendado = false;
            proximoAgendado = false;
            setPesquisouCpf(false);
            setRedirecionaTela(false);
            setOperacaoListando();
            buscarListaEntidades();
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public void voltar() {
        proximoNaoAgendado = false;
        proximoAgendado = false;
    }

    public void proximoNaoAgendado() {
        try {
            Atendimento atendimentoExistente = atendimentoService.findBySenhaAndData(atendimento.getSenha(), new Date());
            if (atendimentoExistente != null) {
                showErrorMessage("form:senhaNaoAgendado", getMessage("mensagemErroSenhaExistente"));
                return;
            }
            proximoNaoAgendado = true;
            cadastrandoNaoAgendado = true;
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    public void proximoAgendado() {
        proximoAgendado = true;
    }

    public void adicionarArquivo() {
        if (arquivo != null) {
            atendimento.getArquivos().add(arquivo);
            arquivo = new Arquivo();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (event != null && event.getFile() != null) {
            arquivo.setArquivo(event.getFile().getContents());
            arquivo.setDescricao(event.getFile().getFileName().trim());
        }
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

    public void excluirArquivo() {
        atendimento.getArquivos().remove(arquivo);
        arquivo = new Arquivo();
    }

    public void buscaPessoaAtendimento(String cpf) {
        Pessoa pessoa = new Pessoa();
        try {
            pessoa.setCpf(cpf);
            pessoa = buscaPessoa(pessoa);
            atendimento.setBeneficiario(pessoa);
            if (pessoa.getEndereco() == null) {
                pessoa.setEndereco(new Endereco());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage();
        }
    }

    @Override
    public String getNomeFormulario() {
        return "protected/form-painel-atendimento.xhtml";
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

    public PrioridadeContatoEnum[] getPrioridadeContatos() {
        return PrioridadeContatoEnum.values();
    }

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }

    public List<VwPainelAtendimento> getAtendimentosAgendados() {
        return atendimentosAgendados;
    }

    public List<VwPainelAtendimento> getAtendimentosNaoAgendados() {
        return atendimentosNaoAgendados;
    }

    public Boolean getCadastrandoNaoAgendado() {
        return cadastrandoNaoAgendado;
    }

    public Boolean getVisualizandoNaoAgendado() {
        return visualizandoNaoAgendado;
    }

    public void incrementaTempoAtendimento() {
        if (atendimento.getTempoAtendimento() == null) atendimento.setTempoAtendimento(0);

        atendimento.setTempoAtendimento(atendimento.getTempoAtendimento() + 1);
    }

    public Boolean getAtendeAtendimentoNaoAgendado() {
        return atendeAtendimentoNaoAgendado;
    }

    public void setAtendeAtendimentoNaoAgendado(Boolean atendeAtendimentoNaoAgendado) {
        this.atendeAtendimentoNaoAgendado = atendeAtendimentoNaoAgendado;
    }

    public Collection<TipoAtendimento> getTipoAtendimentos() {
        return tipoAtendimentos;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }

    public Boolean getProximoNaoAgendado() {
        return proximoNaoAgendado;
    }

    public Collection<TipoBeneficiario> getTipoBeneficiarios() {
        return tipoBeneficiarios;
    }

    public Boolean getProximoAgendado() {
        return proximoAgendado;
    }


}
