package br.gov.pa.igeprev.siaag.repository;

import java.util.Collection;
import java.util.Date;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.model.AgendaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.pa.igeprev.siaag.model.Agenda;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    Collection<Agenda> findByUsuarioId(Integer id);

    Agenda findByUsuarioIdAndData(Integer id, Date data);

    Collection<Agenda> findByUsuarioIdAndDataBetween(Integer id, Date dataInicial, Date dataFinal);

    @Query(value = "SELECT * FROM SIAAG.TAB_AGENDA_ITEM AGI " +
            "JOIN SIAAG.TAB_AGENDA AG ON AGI.ID_AGENDA = AG.ID " +
            "WHERE AGI.ATIVO = 1 AND AG.ATIVO = 1 AND AG.DATA >= :dataAtual AND " +
            "    AGI.ID NOT IN (SELECT AT2.ID_AGENDA_ITEM FROM SIAAG.TAB_ATENDIMENTO AT2 WHERE AT2.ATIVO = 1 AND AT2.ID_SITUACAO_ATENDIMENTO <> 3 AND AT2.ID_SITUACAO_ATENDIMENTO <> 4) " +
            "ORDER BY AG.DATA", nativeQuery = true)
    Collection<Agenda> listarTodasAtivasSemAtendimentoDataAcimaDataAtual(@Param("dataAtual") Date dataAtual);

    @Query(value = "SELECT * FROM SIAAG.VW_AGENDAS_DISPONIVEIS VW WHERE VW.TIPO_AGENDAMENTO = UPPER(:formaAgendamento)", nativeQuery = true)
    Collection<Agenda> listarDatasDisponiveisPorTipoAgendamento(@Param("formaAgendamento") String formaAgendamento);

    Collection<Agenda> findByDataAndAtendimentoNaoAgendadoAndUsuarioId(Date data, Boolean atendimentoNaoAgendado, Integer usuarioId);

    void removeAllByUsuarioId(Integer usuarioId);

}
