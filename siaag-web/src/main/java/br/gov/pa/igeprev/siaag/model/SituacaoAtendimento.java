package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_SITUACAO_ATENDIMENTO")
public class SituacaoAtendimento {
    @Id
    private Integer id;

    @Column(nullable = false, length = 20)
    private String descricao;

    public SituacaoAtendimento(Integer id){
        this.id = id;
    }
}
