package br.gov.pa.igeprev.siaag.model.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 *  View do eprev para consulta de dados de pessoa.
 *
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "vw_cadastro_siaag")
public class VwCadastroSiaag {
    @Id
    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nome_pessoa")
    private String nome;

    @Column(name = "data_nascimento")
    private Date dataNascimento;

    @Column
    private String cep;

    @Column
    private String uf;

    @Column
    private String municipio;

    @Column
    private String bairro;

    @Column
    private String logradouro;

    @Column(name = "tipo_logradouro")
    private String tipoLogradouro;

    @Column
    private String complemento;

    @Column
    private String email;

    @Column(name = "fone")
    private String telefone;

    @Column(name = "tipo_pessoa")
    private String tipoPessoa;
}
