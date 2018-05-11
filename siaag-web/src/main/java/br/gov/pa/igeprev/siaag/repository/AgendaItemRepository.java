package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.Agenda;
import br.gov.pa.igeprev.siaag.model.AgendaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;

public interface AgendaItemRepository extends JpaRepository<AgendaItem, Integer> {

    Collection<AgendaItem> findByAgendaId(Integer id);

    @Query(value = "SELECT * FROM SIAAG.TAB_AGENDA_ITEM AGI " +
            "            JOIN SIAAG.TAB_AGENDA AG ON AGI.ID_AGENDA = AG.ID " +
            "            WHERE AGI.ATIVO = 1 AND AG.ATIVO = 1 AND AG.DATA = :data AND AGI.TIPO_AGENDAMENTO = UPPER(:formaAtendimento)  " +
            "            ORDER BY AG.DATA", nativeQuery = true)
    Collection<AgendaItem> listarTodasAtivasSemAtendimentoDataAcimaData(@Param("data") Date data, @Param("formaAtendimento") String formaAtendimento);

    @Query(value = "SELECT * FROM SIAAG.TAB_AGENDA_ITEM AGI\n" +
            "JOIN SIAAG.TAB_AGENDA AG ON AGI.ID_AGENDA = AG.ID\n" +
            "WHERE AG.DATA = :data AND \n" +
            "      AGI.TIPO_AGENDAMENTO = UPPER(:formaAtendimento) AND\n" +
            "      :horarioInicio BETWEEN AGI.ID_HORARIO_INICIO AND AGI.ID_HORARIO_FIM AND \n" +
            "      AG.ATIVO = 1 AND\n" +
            "      AGI.ATIVO = 1 AND\n" +
            "      AGI.ID NOT IN (SELECT AT2.ID_AGENDA_ITEM FROM SIAAG.TAB_ATENDIMENTO AT2 WHERE AT2.ATIVO = 1 AND AT2.ID_SITUACAO_ATENDIMENTO <> 3 AND AT2.ID_SITUACAO_ATENDIMENTO <> 4)", nativeQuery = true)
    Collection<AgendaItem> listarAgendaItemDisponivelPorDataHorarioAndFormaAtendimento(@Param("data") Date data, @Param("horarioInicio") Integer horarioInicio, @Param("formaAtendimento") String formaAtendimento);


    AgendaItem findByAgendaIdAndId(Integer agendaId, Integer agendaItemId);

    @Query(value = "SELECT * FROM SIAAG.TAB_AGENDA_ITEM WHERE ID_AGENDA = :agendaId AND (:horarioInicioId BETWEEN ID_HORARIO_INICIO AND ID_HORARIO_FIM) AND " +
            "(:horarioFimId BETWEEN ID_HORARIO_INICIO AND ID_HORARIO_FIM)", nativeQuery = true)
    Collection<AgendaItem> findByAgendaHorarioInicioAndHorarioFim(@Param("agendaId") Integer agendaId, @Param("horarioInicioId") Integer horarioInicioId, @Param("horarioFimId") Integer horarioFimId);

    Collection<AgendaItem> findByAgendaUsuarioIdAndHorarioInicioIdAndHorarioFimIdAndAgendaDataBetween(Integer atendenteId, Integer horarioInicioId, Integer horarioFimId, Date dataInicio, Date dataFim);

    Collection<AgendaItem> findByAgendaUsuarioIdAndAgendaDataBetween(Integer usuarioId, Date dataInicio, Date dataFim);

    Collection<AgendaItem> findByAgendaUsuarioIdAndAgendaData(Integer usuarioId, Date data);
}
