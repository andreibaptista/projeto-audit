package br.gov.pa.igeprev.siaag.model;

import br.gov.pa.igeprev.siaag.enumeration.TipoAgendamentoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.primefaces.model.DefaultScheduleEvent;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_AGENDA_ITEM")
public class AgendaItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TipoAgendamentoEnum tipoAgendamento;

    @ManyToOne
    @JoinColumn(name = "id_horario_inicio", referencedColumnName = "id", nullable = false)
    private Horario horarioInicio;

    @ManyToOne
    @JoinColumn(name = "id_horario_fim", referencedColumnName = "id", nullable = false)
    private Horario horarioFim;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agenda", referencedColumnName = "id", nullable = false)
    private Agenda agenda = new Agenda();

    @Transient
    private DefaultScheduleEvent evento;

    @Transient
    private Collection<Horario> horarioInicioDisponiveis;
}
