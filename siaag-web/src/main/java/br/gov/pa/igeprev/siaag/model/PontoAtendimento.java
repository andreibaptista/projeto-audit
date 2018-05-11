package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_PONTO_ATENDIMENTO")
public class PontoAtendimento {
    @Id
    private Integer id;

    @Column(length = 100, nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_endereco", referencedColumnName = "id", nullable = true)
    private Endereco endereco;

    public String formatado() {
        try {
            if(endereco != null) return nome + " - " + endereco.formatado();
            else return nome;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
