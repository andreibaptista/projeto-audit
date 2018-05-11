package br.gov.pa.igeprev.siaag.enumeration;

public enum PaginasContentEnum {

    DEFAULT_PAGE("/pages/protected/default-page.xhtml"),
    HOME("/pages/protected/home.xhtml"),
    FORM_USUARIO_ADMIN("/pages/protected/admin/form-usuario-admin.xhtml"),
    FORM_AGENDA_ATENDENTES("/pages/protected/admin/form-agenda.xhtml"),
    FORM_ATENDIMENTO("/pages/protected/form-atendimento.xhtml"),
    FORM_PAINEL_ATENDIMENTO("/pages/protected/form-painel-atendimento.xhtml");

    private String msg;

    PaginasContentEnum(String tipo) {
        this.msg = tipo;
    }

    public String getMsg() {
        return msg;
    }

    public static boolean contains(String msg) {
        for (PaginasContentEnum ad : PaginasContentEnum.values()) {
            if (msg.equals(ad.getMsg()))
                return true;
        }
        return false;
    }

    public String toString() {
        return this.msg;
    }
}
	
	
