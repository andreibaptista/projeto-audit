package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the enderecoWEB database table.
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnderecoWeb  {
    private String cep;

    private String logradouro;

    private String complemento;

    private String localidade;

    private String bairro;

    private String uf;

    private String unidade;

    private String ibge;

    private String gia;

    private Boolean erro;
}