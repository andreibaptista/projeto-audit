/*
 *  OperacaoBancoEnum
 *  
 *  1.0.0
 *  
 *  © Copyright 2017, Tribunal de Justiça do Estado Roraima
 *  http://www.tjrr.jus.br
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.enumeration;

/**
 * Enum que lista as operações que um registro no banco pode sofrer.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 08/02/2017
 */
public enum OperacaoBancoEnum {

	INCLUSAO(1, "Inclusão"),
	ATUALIZACAO(2, "Alteração"),
	EXCLUSAO(3, "Exclusão");
		
	private int key;
	private String value;
	
	private OperacaoBancoEnum(int key, String value) {
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
