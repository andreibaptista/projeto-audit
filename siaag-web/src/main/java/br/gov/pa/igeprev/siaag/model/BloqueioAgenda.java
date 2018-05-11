package br.gov.pa.igeprev.siaag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_BLOQUEIO_AGENDA")
public class BloqueioAgenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(length = 20, nullable = false)
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    @Column(length = 20, nullable = false)
    private Date dataFim;

    @ManyToOne
    @JoinColumn(name = "id_horario_inicio", referencedColumnName = "id", nullable = false)
    private Horario horarioInicio;

    @ManyToOne
    @JoinColumn(name = "id_horario_fim", referencedColumnName = "id", nullable = false)
    private Horario horarioFim;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

}
