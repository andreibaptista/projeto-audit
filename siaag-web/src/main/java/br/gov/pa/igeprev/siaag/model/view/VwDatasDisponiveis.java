package br.gov.pa.igeprev.siaag.model.view;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vw_datas_disponiveis")
public class VwDatasDisponiveis {

    @Id
    @Column(name = "data")
    private Date data;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_agendamento", nullable = false)
    private FormaAtendimentoEnum formaAtendimento;

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
