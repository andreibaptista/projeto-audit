package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

/**
 * Classe reponsável por realizar as transações com o banco de dados da tabela Perfil.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {

    Horario findByIdAndAtivo(Integer id, Boolean ativo);

    @Query(value = "SELECT * FROM siaag.tab_horario h " +
            "WHERE h.ATIVO = 1 AND h.id > (SELECT max(ai.id_horario_fim) FROM siaag.tab_agenda_item ai " +
            "               WHERE ai.id_agenda = :agendaId) ", nativeQuery = true)
    Collection<Horario> findHorarioInicioDisponivelByAgendaId(@Param("agendaId") Integer agendaId);

    @Query(value = "SELECT * FROM SIAAG.TAB_HORARIO H WHERE H.ATIVO = :ativo AND TO_CHAR(HORARIO, 'hh24:mi:ss') = TO_CHAR(:horario, 'hh24:mi:ss')", nativeQuery = true)
    Horario findByHorarioAndAtivo(@Param("horario") Date horario, @Param("ativo") Boolean ativo);

    Collection<Horario> findByAtivo(Boolean ativo);

    @Query(value = "SELECT * FROM SIAAG.TAB_HORARIO H WHERE H.ID = (SELECT MIN(H2.ID) FROM SIAAG.TAB_HORARIO H2 WHERE H2.ATIVO = 1)" ,nativeQuery = true)
    Horario findPrimeiroHorarioAtivo();

    @Query(value = "SELECT * FROM SIAAG.TAB_HORARIO H WHERE H.ID = (SELECT MAX(H2.ID) FROM SIAAG.TAB_HORARIO H2 WHERE H2.ATIVO = 1)" ,nativeQuery = true)
    Horario findUltimoHorarioAtivo();

    @Query(value = "SELECT * FROM SIAAG.TAB_HORARIO H WHERE TO_CHAR(HORARIO, 'hh24:mi:ss') = TO_CHAR(:horario, 'hh24:mi:ss')", nativeQuery = true)
    Horario findByHorario(@Param("horario") Date horario);
}
