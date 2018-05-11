package br.gov.pa.igeprev.siaag.model;

import javax.persistence.*;

import br.gov.pa.igeprev.siaag.enumeration.*;
import br.gov.pa.igeprev.siaag.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_ATENDIMENTO")
public class Atendimento {
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
    @JoinColumn(name = "id_horario_inicio", referencedColumnName = "id")
    private Horario horarioInicio;
    @ManyToOne
    @JoinColumn(name = "id_horario_fim", referencedColumnName = "id")
    private Horario horarioFim;

    @Column
    private Boolean ativo = true;

    @Temporal(TemporalType.DATE)
    @Column(name = "data", nullable = false)
    private Date data;

    @Column(name = "senha")
    private Integer senha;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @Column(name = "tempo_atendimento")
    private Integer tempoAtendimento = 0;

    @Column(name = "informacoes_atendimento", length = 1000)
    private String informacoesAtendimento;

    @Column
    private Boolean prioritario = false;

    @Column(name = "alterar_dados")
    private Boolean alterarDados = false;

    @ManyToOne
    @JoinColumn(name = "id_responsavel", referencedColumnName = "id")
    private Pessoa responsavel = new Pessoa();

    @Transient
    private List<Arquivo> arquivos = new ArrayList<>();

    public String dataAtendimentoNaoAgendadoFormatado() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (dataNaoAgendado != null) return sdf.format(dataNaoAgendado);
        return "";
    }

    public String dataAtendimentoAgendadoFormatado() {
        return dataFormatada() + " " + horarioInicio.horarioFormatado();
    }

    public String tempoAtendimentoFormatado() {
        String resultado = "";
        Integer minutos = 0;
        Integer horas = 0;
        Integer segundos = 0;
        if (tempoAtendimento != null) {
            segundos = tempoAtendimento;
            if (segundos >= 60) {
                minutos = segundos / 60;
                segundos = segundos % 60;
            }
            if (minutos >= 60) {
                horas = minutos / 60;
                minutos = minutos % 60;
            }
        }
        return String.format("%1$02d", horas) + ":" + String.format("%1$02d", minutos) + ":" + String.format("%1$02d", segundos);
    }

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

    public String dataNaoAgendadoFormatada(){
        try {
            return DataUtils.Formatada(dataNaoAgendado);
        } catch (Exception e) {
            return "";
        }
    }
    public String horaNaoAgendadoFormatada(){
        try {
            return DataUtils.FormatoDataHoraMinuto(dataNaoAgendado);
        } catch (Exception e) {
            return "";
        }
    }
}
