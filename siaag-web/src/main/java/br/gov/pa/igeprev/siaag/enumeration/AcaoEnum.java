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
 * Enum que contém as situações das dívidas ativas/inativas.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 02/03/2017
 */
public enum AcaoEnum {
	
	PENDENTE(1, "Pendente"),
	ENCAMINHADO(2, "Encaminhado"),
	ACEITO (3, "Aceito"),
	ENVIADO (4, "Enviado"),
	DEVOLVIDO (5, "Devolvido"),
	QUITADO (6, "Quitado"),
	ASSINADO(7, "Assinado"),
	PRESCRITO(8,"Prescrito"),
	CADASTRADO_FUNDEJURR(9,"Cadastrado no FUNDEJURR");
	
	
	private int key;
	private String value;
	
	private AcaoEnum(int key, String value) {
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
