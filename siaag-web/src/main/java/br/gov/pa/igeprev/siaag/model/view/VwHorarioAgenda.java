package br.gov.pa.igeprev.siaag.model.view;

import br.gov.pa.igeprev.siaag.model.Agenda;
import br.gov.pa.igeprev.siaag.model.Horario;
import br.gov.pa.igeprev.siaag.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vw_horario_agenda")
public class VwHorarioAgenda {

    @Id
    @Column(name="id_agenda_item")
    private Integer idAgendaItem;

    @ManyToOne
    @JoinColumn(name = "id_agenda", referencedColumnName = "id", nullable = false)
    private Agenda agenda;

    @ManyToOne
    @JoinColumn(name = "id_horario_inicio_agenda", referencedColumnName = "id", nullable = false)
    private Horario horarioInicioAgendaItem;

    @ManyToOne
    @JoinColumn(name = "id_horario_fim_agenda", referencedColumnName = "id", nullable = false)
    private Horario horarioFimAgendaItem;

    @ManyToOne
    @JoinColumn(name = "id_horario_inicio_bloqueio", referencedColumnName = "id")
    private Horario horarioInicioBloqueio;

    @ManyToOne
    @JoinColumn(name = "id_horario_fim_bloqueio", referencedColumnName = "id")
    private Horario horarioFimBloqueio;

    @ManyToOne
    @JoinColumn(name = "id_atendente", referencedColumnName = "id")
    private Usuario atendente;

    @Temporal(TemporalType.DATE)
    @Column
    private Date data;

}
