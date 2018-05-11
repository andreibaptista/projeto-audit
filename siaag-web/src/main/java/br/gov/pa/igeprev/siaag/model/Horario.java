package br.gov.pa.igeprev.siaag.model;

import br.gov.pa.igeprev.siaag.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_HORARIO")
public class Horario implements Comparable<Horario> {
    @Id
    private Integer id;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "hh:mm")
    @Column(nullable = false)
    private Date horario;

    @Transient
    public Pessoa atendente;

    @Column
    private Boolean ativo = true;

    public Horario(Integer id) {
        this.id = id;
    }

    public String horarioFormatado() {
        return DataUtils.FormatoHorarioHoraMinuto(this.horario);
    }

    @Override
    public int compareTo(Horario o) {
        if(this.id != null) {
            if (this.id < o.id) {
                return -1;
            }
            if (this.id > o.id) {
                return 1;
            }
        }
        return 0;
    }
}
