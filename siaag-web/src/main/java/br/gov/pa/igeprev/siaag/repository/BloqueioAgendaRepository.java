package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.BloqueioAgenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

public interface BloqueioAgendaRepository extends JpaRepository<BloqueioAgenda, Integer> {
    Collection<BloqueioAgenda> findByUsuarioId(@NotNull Integer id);

    @Query(value = "SELECT * FROM SIAAG.TAB_BLOQUEIO_AGENDA BA WHERE BA.ID_USUARIO = :usuarioId AND :data BETWEEN BA.DATA_INICIO AND BA.DATA_FIM", nativeQuery = true)
    Collection<BloqueioAgenda> findByUsuarioIdAndData(@Param("usuarioId") Integer usuarioId,@Param("data") Date data);
}
