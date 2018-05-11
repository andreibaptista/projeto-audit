package br.gov.pa.igeprev.siaag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_TIPO_BENEFICIARIO")
public class TipoBeneficiario {
    @Id
    private Integer id;

    @Column(length = 30)
    @NotNull
    private String descricao;

    public TipoBeneficiario(Integer id) {
        this.id = id;
    }
}
