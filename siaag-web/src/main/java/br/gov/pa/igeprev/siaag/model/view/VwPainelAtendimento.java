package br.gov.pa.igeprev.siaag.model.view;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.enumeration.PrioridadeContatoEnum;
import br.gov.pa.igeprev.siaag.model.*;
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
@Table(name = "VW_PAINEL_ATENDIMENTO")
public class VwPainelAtendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_requerente", referencedColumnName = "id", nullable = false)
    private Pessoa requerente = new Pessoa();

    @ManyToOne
    @JoinColumn(name = "id_beneficiario", referencedColumnName = "id", nullable = false)
    private Pessoa beneficiario = new Pessoa();

    @ManyToOne
    @JoinColumn(name = "id_tipo_requerente", referencedColumnName = "id", nullable = false)
    private TipoRequerente tipoRequerente;

    @ManyToOne
    @JoinColumn(name = "id_tipo_beneficiario", referencedColumnName = "id", nullable = false)
    private TipoBeneficiario tipoBeneficiario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_atendimento", referencedColumnName = "id", nullable = false)
    private TipoAtendimento tipoAtendimento;

    @ManyToOne
    @JoinColumn(name = "id_ponto_atendimento", referencedColumnName = "id", nullable = false)
    private PontoAtendimento pontoAtendimento;

    @ManyToOne
    @JoinColumn(name = "id_atendente", referencedColumnName = "id", nullable = false)
    private Pessoa atendente = new Pessoa();

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_atendimento", nullable = false, length = 15)
    private FormaAtendimentoEnum formaAtendimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade_contato", nullable = false, length = 15)
    private PrioridadeContatoEnum prioridadeContato = PrioridadeContatoEnum.EMAIL;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_nao_agendado")
    private Date dataNaoAgendado;

    @ManyToOne
    @JoinColumn(name = "id_situacao_atendimento", referencedColumnName = "id", nullable = false)
    private SituacaoAtendimento situacaoAtendimento;

    @ManyToOne
    @JoinColumn(name = "id_usuario_cadastrou", referencedColumnName = "id", nullable = false)
    private Usuario usuarioCadastrou;

    @ManyToOne
    @JoinColumn(name = "id_horario_inicio", referencedColumnName = "id", nullable = false)
    private Horario horarioInicio;
    @ManyToOne
    @JoinColumn(name = "id_horario_fim", referencedColumnName = "id", nullable = false)
    private Horario horarioFim;

    @Column
    private Boolean ativo = true;

    @Temporal(TemporalType.DATE)
    @Column(name="data", nullable = false)
    private Date data;

    @Column(name="senha")
    private Integer senha;

    @Column(name="observacao", length = 500)
    private String observacao;

    public String getDataExtenso() {
        if(data != null){
            return DataUtils.DataPorExtenso(data);
        }
        return "";
    }

    public String dataFormatada(){
        try{
            return DataUtils.Formatada(data);
        }catch (Exception e){
            return "";
        }
    }
}
