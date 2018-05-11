package br.gov.pa.igeprev.siaag.model;

import br.gov.pa.igeprev.siaag.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_AGENDA")
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(length = 20, nullable = false)
    private Date data;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @Column(name = "atendimento_nao_agendado")
    private Boolean atendimentoNaoAgendado = false;

    @Transient
    private Collection<AgendaItem> agendaItems;


    public String getDataExtenso() {
        if (data != null) {
            return DataUtils.DataPorExtenso(data);
        }
        return "";
    }

    public String dataFormatada() {
        try {
            return DataUtils.Formatada(data);
        } catch (Exception e) {
            return "";
        }
    }

}
