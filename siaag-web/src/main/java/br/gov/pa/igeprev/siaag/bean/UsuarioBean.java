package br.gov.pa.igeprev.siaag.bean;

import br.gov.pa.igeprev.siaag.enumeration.PerfilEnum;
import br.gov.pa.igeprev.siaag.enumeration.TipoAgendamentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.*;
import br.gov.pa.igeprev.siaag.service.*;
import br.gov.pa.igeprev.siaag.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.util.*;

@ManagedBean
@ViewScoped
@Controller
@Named
public class UsuarioBean extends ManagerBeanDefault {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PerfilService perfilService;
    @Autowired
    private SetorService setorService;
    @Autowired
    private PontoAtendimentoService pontoAtendimentoService;
    @Autowired
    private PontoAtendimentoSetorService pontoAtendimentoSetorService;
    @Autowired
    private AgendaService agendaService;
    @Autowired
    private HorarioService horarioService;
    @Autowired
    private AtendimentoService atendimentoService;

    private Usuario usuario = new Usuario();
    private PontoAtendimento pontoAtendimento = new PontoAtendimento();
    private Setor setor = new Setor();

    private Collection<Usuario> usuarios = new ArrayList<>();
    private Collection<PontoAtendimento> pontoAtendimentos = new ArrayList<>();
    private Collection<Setor> setores = new ArrayList<>();
    private List<Perfil> perfils = new ArrayList<>();
    private Perfil perfilAntigo = new Perfil();

    private String novaSenha;
    private String confirmacaoNovaSenha;

    //Parametros de pesquisa
    private String nomePesq = "";
    private Perfil perfilPesq = new Perfil();


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void inicializacao() throws ServiceException {
        novaSenha = "";
        confirmacaoNovaSenha = "";
        usuario = new Usuario();
        pontoAtendimentos = pontoAtendimentoService.findAll();
        perfils = new ArrayList<>(perfilService.findAll());
        usuario = new Usuario();
        pontoAtendimento = new PontoAtendimento();
        setor = new Setor();
        usuario.setPerfil(new Perfil(1));
        if (!segurancaBean.autenticado()) {
            segurancaBean.setCadastro(true);
            return;
        } else {
            if (segurancaBean.getUsuarioLogado().getPrimeiroAcesso()) {
                usuario = segurancaBean.getUsuarioLogado();
            }
        }
        buscarListaEntidades();

    }

    public void carregaSetores() {
        try {
            if (usuario.getPerfil() != null && usuario.getPerfil().getId() == null)
                usuario.setPerfil(perfilService.findById(PerfilEnum.ADMINISTRADOR.getId()));
            if (usuario.getPerfil() != null && usuario.getPerfil().getId() != PerfilEnum.CLIENTE.getId()) {
                if (pontoAtendimento != null && pontoAtendimento.getId() != null) {
                    setores = setorService.findByPontoAtendimentoId(pontoAtendimento.getId());
                }
            } else {
                pontoAtendimento = new PontoAtendimento();
                setor = new Setor();
                usuario.setPontoAtendimentoSetor(new PontoAtendimentoSetor());
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void pesquisaUsuarios() {
        try {
            if (perfilPesq.getId() != null) {
                usuarios = usuarioService.findByPerfilIdAndPessoaNome(perfilPesq.getId(), nomePesq);
            } else {
                usuarios = usuarioService.findByPessoaNome(nomePesq);
            }
            usuarios.remove(segurancaBean.getUsuarioLogado());
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    protected void novoRegistro() {
        usuario = new Usuario();
        pontoAtendimento = new PontoAtendimento();
        setor = new Setor();
        removePerfilTodos();
    }

    @Override
    protected void editarRegistro(Object obj) {
        usuario = (Usuario) obj;
        if (usuario.getPontoAtendimentoSetor() != null &&
                usuario.getPontoAtendimentoSetor().getId() != null) {
            pontoAtendimento = usuario.getPontoAtendimentoSetor().getPontoAtendimento();
            carregaSetores();
            setor = usuario.getPontoAtendimentoSetor().getSetor();
        }
        removePerfilTodos();
        segurancaBean.setObjeto(null);
        perfilAntigo = usuario.getPerfil();
    }

    @Override
    protected void visualizarRegistro(Object obj) {
        usuario = (Usuario) obj;
        if (usuario.getPontoAtendimentoSetor() != null &&
                usuario.getPontoAtendimentoSetor().getId() != null) {
            pontoAtendimento = usuario.getPontoAtendimentoSetor().getPontoAtendimento();
            carregaSetores();
            setor = usuario.getPontoAtendimentoSetor().getSetor();
        }
        removePerfilTodos();
    }

    @Override
    protected void cancelarOperacao() throws ServiceException {
        buscarListaEntidades();
    }

    @Override
    protected void buscarListaEntidades() throws ServiceException {
        removePerfilTodos();
        perfils.add(0, new Perfil(null, "Todos"));
        pesquisaUsuarios();
    }

    private void removePerfilTodos(){
        Perfil perfilTodos = new Perfil(null,"Todos");
        perfils.remove(perfilTodos);
    }

    @Override
    protected void remover() throws ServiceException {
        if (usuario != null && usuario.getId() != null) usuarioService.excluir(usuario.getId());
    }

    @Override
    protected void salvar() throws ServiceException {
        if (isCadastrando()) {
            usuario.setSenha("");
            usuario.setPrimeiroAcesso(true);
        } else {
            if (novaSenha != null && !novaSenha.isEmpty()) {
                if (novaSenha.length() >= 6) {
                    usuario.setSenha(encoder.encode(novaSenha));
                } else {
                    showErrorMessage("form:msgSenha", getMessage("validacaoSenhaMenor6Caracteres"));
                    setErroValidacao(true);
                }
            }
        }

        if (validaCamposPerfisInternos()) {
            usuario.setAtivo(true);
            if (perfilAntigo != null && perfilAntigo.getId() != null &&
                    perfilAntigo.getId() == PerfilEnum.ATENDENTE.getId() && !perfilAntigo.equals(usuario.getPerfil())) {
                Collection<Atendimento> atendimentosExistentes = atendimentoService.findByAtendenteIdAndDataGreaterThanEqual(usuario.getPessoa().getId(), DataUtils.SomenteData(new Date()));
                if (atendimentosExistentes != null && !atendimentosExistentes.isEmpty()) {
                    showErrorMessage(getMessage("mensagemErroUsuarioAtendenteComAgendamento"));
                    setErroValidacao(true);
                    return;
                }
            }
            usuario = usuarioService.salvar(usuario);
            if ((isCadastrando() && usuario.getPerfil().getId() == PerfilEnum.ATENDENTE.getId()) ||
                    (perfilAntigo != null && perfilAntigo.getId() != null && perfilAntigo.getId() != PerfilEnum.ATENDENTE.getId() && usuario.getPerfil().getId() == PerfilEnum.ATENDENTE.getId())) {
                salvarAgendaUsuarioAtendente();
            } else if (perfilAntigo != null && perfilAntigo.getId() != null &&
                    perfilAntigo.getId() == PerfilEnum.ATENDENTE.getId() && !usuario.getPerfil().getId().equals(perfilAntigo.getId())) {
                agendaService.removeByUsuarioId(usuario.getId());
            }
            if (usuario.getId().equals(segurancaBean.getUsuarioLogado().getId())) {
                redirect("/siaag/logout");
            }
            usuario = new Usuario();
        } else {
            setErroValidacao(true);
        }

    }

    private void salvarAgendaUsuarioAtendente() {
        try {
            if (isEditando()) {

            }
            Collection<Agenda> agendasPeriodo = new ArrayList<>();
            List<Date> datas;
            Calendar calHoje = Calendar.getInstance();
            calHoje.set(calHoje.get(Calendar.YEAR), calHoje.get(Calendar.MONTH), calHoje.get(Calendar.DATE), 0, 0, 0);
            datas = DataUtils.DatasAno(calHoje.getTime());
            Horario primeiroHorarioAtivo = horarioService.findPrimeiroHorarioAtivo();
            Horario ultimohorariosAtivo = horarioService.findUltimoHorarioAtivo();
            for (Date dt : datas) {
                Agenda agenda = new Agenda();
                agenda.setUsuario(usuario);
                agenda.setAtivo(true);
                agenda.setData(dt);
                agenda.setAtendimentoNaoAgendado(true);
                AgendaItem agendaItem = new AgendaItem();
                agendaItem.setHorarioInicio(primeiroHorarioAtivo);
                agendaItem.setHorarioFim(ultimohorariosAtivo);
                agendaItem.setTipoAgendamento(TipoAgendamentoEnum.AGENDADO);
                agendaItem.setAtivo(true);
                Collection<AgendaItem> agendaItems = new ArrayList<>();
                agendaItems.add(agendaItem);
                agenda.setAgendaItems(agendaItems);
                agendasPeriodo.add(agenda);
            }
            agendaService.salvar(agendasPeriodo, null);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    private boolean validaCamposPerfisInternos() {
        if (usuario.getPerfil().getId() != PerfilEnum.CLIENTE.getId()) {
            if (pontoAtendimento == null || (pontoAtendimento.getId() != null && pontoAtendimento.getId() == null)) {
                showErrorMessage(getMessage("campoObrigatorioPontoAtendimento"));
            }
            if (setor == null || setor.getId() == null) {
                showErrorMessage(getMessage("campoObrigatorioSetor"));
            }

            if ((pontoAtendimento == null || (pontoAtendimento.getId() != null && pontoAtendimento.getId() == null)) ||
                    (setor == null || (setor != null && setor.getId() == null))) {
                return false;
            }
            usuario.setPontoAtendimentoSetor(pontoAtendimentoSetorService.findByPontoAtendimentoIdAndSetorId(pontoAtendimento.getId(), setor.getId()));
            return true;
        } else {
            usuario.setPontoAtendimentoSetor(null);
            return true;
        }
    }

    @Override
    public String getNomeFormulario() {
        if (segurancaBean.autenticado()) {
            return "protected/form-usuario.xhtml";
        }
        return "public/form-usuario.xhtml";
    }

    @Override
    protected String getMsgTraceAlteracao() {
        return "Alterou o objeto " + usuario.getClass().getName() + " com id " + usuario.getId();
    }

    @Override
    protected String getMsgTraceInclusao() {
        return "Cadastrou o objeto " + usuario.getClass().getName() + " com id " + usuario.getId();
    }

    @Override
    protected String getMsgTraceExclusao() {
        return "Excluiu o objeto " + usuario.getClass().getName() + " com id " + usuario.getId();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Collection<Usuario> getUsuarios() {
        return usuarios;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmacaoNovaSenha() {
        return confirmacaoNovaSenha;
    }

    public void setConfirmacaoNovaSenha(String confirmacaoNovaSenha) {
        this.confirmacaoNovaSenha = confirmacaoNovaSenha;
    }

    public Collection<PontoAtendimento> getPontoAtendimentos() {
        return pontoAtendimentos;
    }

    public Collection<Setor> getSetores() {
        return setores;
    }

    public Collection<Perfil> getPerfils() {
        return perfils;
    }

    public PontoAtendimento getPontoAtendimento() {
        return pontoAtendimento;
    }

    public void setPontoAtendimento(PontoAtendimento pontoAtendimento) {
        this.pontoAtendimento = pontoAtendimento;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public String getNomePesq() {
        return nomePesq;
    }

    public void setNomePesq(String nomePesq) {
        this.nomePesq = nomePesq;
    }

    public Perfil getPerfilPesq() {
        return perfilPesq;
    }

    public void setPerfilPesq(Perfil perfilPesq) {
        this.perfilPesq = perfilPesq;
    }

    public Perfil getPerfilAntigo() {
        return perfilAntigo;
    }

    public void setPerfilAntigo(Perfil perfilAntigo) {
        this.perfilAntigo = perfilAntigo;
    }

}
