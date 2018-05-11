package br.gov.pa.igeprev.siaag.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.gov.pa.igeprev.siaag.enumeration.PaginasContentEnum;
import br.gov.pa.igeprev.siaag.enumeration.PaginasEnum;
import br.gov.pa.igeprev.siaag.enumeration.PerfilEnum;
import br.gov.pa.igeprev.siaag.model.Usuario;
import br.gov.pa.igeprev.siaag.repository.UsuarioRepository;

@ManagedBean
@SessionScoped
@Controller
@Model
public class SegurancaBean extends Bean {
    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String nomeUsuario = "";

    private Usuario usuarioLogado;

    private PerfilEnum perfilEnum;

    private PaginasContentEnum contentPage = PaginasContentEnum.DEFAULT_PAGE;

    private Boolean cadastro = false; //TODO: Melhorar solução.

    private Object objeto;

    @PostConstruct
    public void posContructor() throws Exception {
        usuarioLogado = buscaUsuarioLogado();
        if(usuarioLogado == null){
            FacesContext.getCurrentInstance ().getExternalContext().redirect("/siaag/logout"); //TODO: workaround
        }
    }

    @PreDestroy
    public void destroy() {
        usuarioLogado = null;
    }

    public void trocarItemMenu(String itemMenu) {
        if (itemMenu.equals("FormUsuarioAdmin")) {
            contentPage = PaginasContentEnum.FORM_USUARIO_ADMIN;
        } else if (itemMenu.equals("FormAgendaAtendentes")){
            contentPage = PaginasContentEnum.FORM_AGENDA_ATENDENTES;
        }else if (itemMenu.equals("FormAtendimento")){
            contentPage = PaginasContentEnum.FORM_ATENDIMENTO;
        }else if (itemMenu.equals("FormPainelAtendimento")){
            contentPage = PaginasContentEnum.FORM_PAINEL_ATENDIMENTO;
        } else {
            contentPage = PaginasContentEnum.DEFAULT_PAGE;
        }
        //redirect(PaginasEnum.HOME);
    }

    public String getNomeUsuario() {
        try {
            nomeUsuario = "";
            if (usuarioLogado != null && usuarioLogado.getId() != null) {
                String nomes[] = usuarioLogado.getPessoa().getNome().split(" ");
                for (String string : nomes) {
                    if ((nomeUsuario + " " + string).length() > 19) {
                        break;
                    }
                    nomeUsuario += " " + string;
                }
                return nomeUsuario;
            }
            return "Usuário";
        } catch (Exception e) {
            return "Usuário";
        }
    }

    private Usuario buscaUsuarioLogado() {
        // FIXME: Ajustar o Principal.
        try {
            Usuario usuario = new Usuario();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                usuario.setLogin(user.getUsername());
                if (!user.getAuthorities().isEmpty()) {
                    usuario = usuarioRepository.findByLogin(user.getUsername());
                    if(usuario.getPrimeiroAcesso()) {
                        redirect("/siaag/logout");
                    }
                }
            } else {
                redirect("/siaag/logout");
                return null;
            }

            return usuario;
        } catch (Exception e) {
            redirect("/siaag/logout");
            return null;
        }
    }

    Boolean autenticado() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof User && authentication.isAuthenticated()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public PerfilEnum getPerfilEnum() {
        return perfilEnum;
    }

    public PaginasContentEnum getContentPage() {
        return contentPage;
    }

    public void setContentPage(PaginasContentEnum contentPage) {
        this.contentPage = contentPage;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public String logoutDo(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        SecurityContextHolder.clearContext();
        session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "logout";
    }

    public Boolean getCadastro() {
        return cadastro;
    }

    public void setCadastro(Boolean cadastro) {
        this.cadastro = cadastro;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }
}
