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


public enum TipoRequerenteEnum {

	PROPRIO(1, "O próprio"),
	CURADOR(2, "Curador"),
	GUARDIAO(3, "Guardião"),
	PROCURADOR(4, "Procurador"),
	REPRESENTANTE_LEGAL(5, "Representante legal"),
	TUTOR(6, "Tutor");

	private int id;
	private String value;

	TipoRequerenteEnum(int id, String value) {
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
