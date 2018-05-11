package br.gov.pa.igeprev.siaag.enumeration;

public enum PaginasEnum {
	
		LOGIN("/pages/login.xhtml"),
		HOME("/siaag"),//TODO: WORKAROUND
		ERRO("error.xhtml");

	  private String msg;
	
	  private PaginasEnum(String tipo) {
	    this.msg = tipo;
	  }
	
	  public String getMsg() {
	    return msg;
	  }
	
	  public static boolean contains(String msg) {
	    for (PaginasEnum ad : PaginasEnum.values()) {
	      if (msg.equals(ad.getMsg()))
	        return true;
	    }
	    return false;
	  }
	
	  public String toString() {
	    return this.msg;
	  }
}
	
	
