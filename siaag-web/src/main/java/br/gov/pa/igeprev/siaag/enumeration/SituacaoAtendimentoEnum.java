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


public enum SituacaoAtendimentoEnum {

    ATIVO(1, "Ativo"),
    REAGENDADO(2, "Reagendado"),
    EXPIRADO(3, "Expirado"),
    CANCELADO(4, "Cancelado"),
    PRE_ATENDIMENTO(5, "Pré-atendimento"),
    PRE_ATENDIDO(6, "Pré-atendido"),
    ATENDIDO(7, "Atendido");

    private int id;
    private String value;

    SituacaoAtendimentoEnum(int id, String value) {
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
