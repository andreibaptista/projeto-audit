package br.gov.pa.igeprev.siaag.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class LoginBean {

    public String redirectCadastrar(String pagina) {
        String pageRedirect = null;
        pageRedirect = (!pagina.contains("?faces-redirect=true")) ? pagina.concat("?faces-redirect=true") : pagina;
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(Boolean.TRUE);
        return pageRedirect;
    }

}
