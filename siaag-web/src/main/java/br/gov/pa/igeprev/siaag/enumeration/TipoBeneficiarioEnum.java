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


public enum TipoBeneficiarioEnum {

	PENSIONISTA(1, "Pensionista"),
	PUBLICO_GERAL(2, "Público em geral"),
	SERVIDOR_ATIVO(3, "Servidor ativo"),
	SERVIDOR_INATIVO(4, "Servidor inativo");

	private int id;
	private String value;

	TipoBeneficiarioEnum(int id, String value) {
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
