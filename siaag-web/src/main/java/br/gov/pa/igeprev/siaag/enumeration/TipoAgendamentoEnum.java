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


public enum TipoAgendamentoEnum {

	AGENDADO(1, "Agendado"),
	NAO_AGENDADO(2, "Não agendado");

	private int id;
	private String value;

	TipoAgendamentoEnum(int id, String value) {
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
