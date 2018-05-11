package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_SETOR")
public class Setor {
    @Id
    private Integer id;

    @Column(length = 50, nullable = false)
    private String descricao;
}
