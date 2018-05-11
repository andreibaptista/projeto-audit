/*
 *  EstadoEnum
 *  
 *  1.0.0
 *  
 *  © Copyright 2017, Tribunal de Justiça do Estado Roraima
 *  http://www.tjrr.jus.br
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.enumeration;

/**
 * Enum que lista os estados brasileiros.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 08/02/2017
 */
public enum EstadoEnum {

	ACRE("Acre", "AC"),
	ALAGOAS("Alagoas", "AL"),
	AMAPA("Amapá", "AP"),
	AMAZONAS("Amazonas", "AM"),
	BAHIA("Bahia", "BA"),	 
	CEARA("Ceará", "CE"),	 
	DISTRITO_FEDERAL("Distrito Federal", "DF"),	 
	ESPIRITO_SANTO("Espírito Santo", "ES"),	 
	GOIAS("Goiás", "GO"),	 
	MARANHAO("Maranhão", "MA"),	 
	MATO_GROSSO("Mato Grosso", "MT"),	 
	MATO_GROSSO_SUL("Mato Grosso do Sul", "MS"),	 
	MINAS_GERAIS("Minas Gerais", "MG"),	 
	PARA("Pará", "PA"),	 
	PARAIBA("Paraíba", "PB"),
	PARANA("Paraná",	"PR"),	 
	PERNAMBUCO("Pernambuco", "PE"),	 
	PIAUI("Piauí", "PI"),	 
	RIO_JANEIRO("Rio de Janeiro", "RJ"),
	RIO_GRANDE_NORTE("Rio Grande do Norte", "RN"),	 
	RIO_GRANDE_SUL("Rio Grande do Sul", "RS"),	 
	RONDONIA("Rondônia", "RO"),	 
	RORAIMA("Roraima", "RR"),	 
	SANTA_CATARINA("Santa Catarina", "SC"),	 
	SAO_PAULO("São Paulo", "SP"),	 
	SERGIPE("Sergipe", "SE"),	 
	TOCANTINS("Tocantins",	"TO");
	
	private String nome;
	private String sigla;
	
	private EstadoEnum(String nome, String sigla) {
		this.nome = nome;
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public String getSigla() {
		return sigla;
	}
}
