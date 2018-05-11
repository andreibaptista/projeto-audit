package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_PONTO_ATENDIMENTO_SETOR")
public class PontoAtendimentoSetor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ponto_atendimento", referencedColumnName = "id", nullable = false)
    private PontoAtendimento pontoAtendimento = new PontoAtendimento();

    @ManyToOne
    @JoinColumn(name = "id_setor", referencedColumnName = "id", nullable = false)
    private Setor setor = new Setor();

}
