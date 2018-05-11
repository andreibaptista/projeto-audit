package br.gov.pa.igeprev.siaag.bean;

import br.gov.pa.igeprev.siaag.enumeration.PerfilEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Usuario;
import br.gov.pa.igeprev.siaag.service.PerfilService;
import br.gov.pa.igeprev.siaag.service.UsuarioService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@ManagedBean
@ViewScoped
@Controller
public class UsuarioExternoBean extends Bean {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(UsuarioExternoBean.class);

    @Autowired
    protected UsuarioService usuarioService;
    @Autowired
    protected PerfilService perfilService;

    private Usuario usuario = new Usuario();

    private String novaSenha;
    private String confirmacaoNovaSenha;
    private Date dataNascimentoRecuperacao;
    private String nomeRecuperacao;
    private String cpfRecuperacao;
    private String matriculaRecuperacao;
    private Boolean podeRecuperarSenha = false;

    @PostConstruct
    public void inicializacao() {
        LOGGER.info("Acessou o formulário " + getNomeFormulario());

        usuario = buscaUsuarioLogado();
        if (usuario != null && usuario.getId() != null && !usuario.getPrimeiroAcesso()) {
            LOGGER.info("Usuário não encontrado ou não era o primeiro acesso.");
            redirect("/siaag/logout");
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            String traceFinal;
            traceFinal = "Saiu do formulário " + getNomeFormulario();
            LOGGER.info(traceFinal);
            logout();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void logout() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = req.getSession(false);
        SecurityContextHolder.clearContext();
        session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        redirect("/siaag/logout");
    }

    private Usuario buscaUsuarioLogado() {
        try {
            Usuario usuario = new Usuario();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                usuario.setLogin(user.getUsername());
                if (!user.getAuthorities().isEmpty()) {
                    usuario = usuarioService.findByLogin(user.getUsername());
                }
            } else {
                return new Usuario();
            }

            return usuario;
        } catch (Exception e) {
            return new Usuario();
        }
    }

    public void salvar() throws ServiceException {
        try {
            usuario.setPerfil(perfilService.findById(PerfilEnum.CLIENTE.getId()));
            usuario.setPontoAtendimentoSetor(null);
            usuario.setAtivo(true);

            usuario = usuarioService.salvar(usuario);

            String trace;
            if (getMsgTraceInclusao().length() == 0) {
                trace = "Cadastro objeto no formulário " + getNomeFormulario();
            } else {
                trace = getMsgTraceInclusao();
            }

            showSuccessMessage();
            LOGGER.info(trace);
            redirect("/siaag/logout");
            usuario = new Usuario();
        } catch (ServiceException e) {
            if(e.getMessage().equals("mensagemERROCpfExistente")) showErrorMessage(getMessage("mensagemErroGenericaMotivo",getMessage(e.getMessage())));
            else showErrorMessage(getMessage(e.getMessage()));
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showErrorMessage();
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void alterarSenhaPrimeiroAcesso() throws ServiceException {
        try {
            if (novaSenha.equals(confirmacaoNovaSenha)) {
                usuario.setSenha(novaSenha);
                usuario.setPrimeiroAcesso(false);
                usuarioService.salvar(usuario);
                novaSenha = "";
                confirmacaoNovaSenha = "";
                podeRecuperarSenha = false;
                redirectWithMessage("/siaag/logout", getMessage("SenhaAlteradaComSucesso"));
            } else {
                showErrorMessage(getMessage("mensagemErroSenhasDiferentes"));
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void recuperarSenha() {
        try {
            if (novaSenha.equals(confirmacaoNovaSenha)) {
                usuario.setSenha(novaSenha);
                usuarioService.salvar(usuario);
                novaSenha = "";
                confirmacaoNovaSenha = "";
                cpfRecuperacao = "";
                nomeRecuperacao = "";
                dataNascimentoRecuperacao = null;
                redirectWithMessage("/siaag/login", getMessage("SenhaAlteradaComSucesso"));
            } else {
                showErrorMessage(getMessage("mensagemErroSenhasDiferentes"));
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void validacaoCamposRecuperacaoSenha() {
        try {
            if (!cpfRecuperacao.isEmpty() && !nomeRecuperacao.isEmpty() && dataNascimentoRecuperacao != null) {
                usuario = usuarioService.findByCpfAndNomeAndDataNascimento(cpfRecuperacao, nomeRecuperacao, dataNascimentoRecuperacao);
                if (usuario != null && usuario.getId() != null) {
                    podeRecuperarSenha = true;
                } else {
                    showErrorMessage(getMessage("mensagemErroUsuarioNaoEncontrato"));
                }
            }
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Boolean usuarioInterno() {
        try {
            if (cpfRecuperacao != null && cpfRecuperacao.length() == 11) {
                Usuario usuarioVerificacao = usuarioService.findByLogin(cpfRecuperacao);
                if (usuarioVerificacao != null && usuarioVerificacao.getId() != null &&
                        usuarioVerificacao.getPerfil().getId() != null && !usuarioVerificacao.getPerfil().getId().equals(PerfilEnum.CLIENTE.getId())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            showErrorMessage();
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public void limpar() {
        novaSenha = "";
        confirmacaoNovaSenha = "";
        dataNascimentoRecuperacao = null;
        nomeRecuperacao = "";
        cpfRecuperacao = "";
        matriculaRecuperacao = "";
        podeRecuperarSenha = false;
        usuario = new Usuario();
    }

    private String getNomeFormulario() {
        return "public/form-usuario.xhtml";
    }

    private String getMsgTraceInclusao() {
        return "Cadastrou o objeto " + usuario.getClass().getName() + " com id " + usuario.getId();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Date getDataNascimentoRecuperacao() {
        return dataNascimentoRecuperacao;
    }

    public void setDataNascimentoRecuperacao(Date dataNascimentoRecuperacao) {
        this.dataNascimentoRecuperacao = dataNascimentoRecuperacao;
    }

    public String getNomeRecuperacao() {
        return nomeRecuperacao;
    }

    public void setNomeRecuperacao(String nomeRecuperacao) {
        this.nomeRecuperacao = nomeRecuperacao;
    }

    public String getCpfRecuperacao() {
        return cpfRecuperacao;
    }

    public void setCpfRecuperacao(String cpfRecuperacao) {
        this.cpfRecuperacao = cpfRecuperacao;
    }

    public Boolean getPodeRecuperarSenha() {
        return podeRecuperarSenha;
    }

    public void setPodeRecuperarSenha(Boolean podeRecuperarSenha) {
        this.podeRecuperarSenha = podeRecuperarSenha;
    }

    public String getMatriculaRecuperacao() {
        return matriculaRecuperacao;
    }

    public void setMatriculaRecuperacao(String matriculaRecuperacao) {
        this.matriculaRecuperacao = matriculaRecuperacao;
    }
}
