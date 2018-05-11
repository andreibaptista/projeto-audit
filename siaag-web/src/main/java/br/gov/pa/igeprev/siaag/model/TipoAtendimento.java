package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_TIPO_ATENDIMENTO")
public class TipoAtendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String descricao;

    @Column(name = "tempo_atendimento", nullable = false)
    private Integer tempoAtendimento;

    @Column(name = "tempo_analise", nullable = false)
    private Integer tempoAnalise;

    @Column(nullable = false)
    private Boolean redirecionamento = false;

    @Column(nullable = false)
    private Boolean ativo = true;

}
