/*
 *  AcaoEnum
 *  
 *  1.0.0
 *  
 *  © Copyright 2017, Tribunal de Justiça do Estado Roraima
 *  http://www.tjrr.jus.br
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.enumeration;

public enum PerfilEnum {
	
	ADMINISTRADOR(1, "Administrador"),
	RECEPCIONISTA(2, "Recepcionista"),
	ATENDENTE(3, "Atendente"),
	CLIENTE(4, "Cliente"),
	TECNICO(5, "Técnico");
	
	private int id;
	private String value;
	
	private PerfilEnum(int id, String value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public String getValue() {
		return value;
	}
}
