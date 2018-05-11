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

/**
 * Enum que contém as informações da dívida.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 02/03/2017
 */
public enum SexoEnum {
	
	FEMININO(0, "Feminino"),
	MASCULINO(1, "Masculino");
		
	private int key;
	private String value;
	
	private SexoEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
