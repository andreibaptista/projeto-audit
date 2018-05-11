package br.gov.pa.igeprev.siaag.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.caelum.stella.format.CPFFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_PESSOA")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(length = 150)
    private String email;

    @Column(length = 10)
    private String telefone1;

    @Column(length = 10)
    private String telefone2;

    @Column(length = 11)
    private String celular;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_nascimento")
    private Date dataNascimento;

    @ManyToOne
    @JoinColumn(name = "id_endereco", referencedColumnName = "id")
    private Endereco endereco = new Endereco();

    public String cpfFormatado() {
		if(cpf != null && !cpf.isEmpty()){
		    return new CPFFormatter().format(cpf);
        }
        return this.cpf;
    }

}
