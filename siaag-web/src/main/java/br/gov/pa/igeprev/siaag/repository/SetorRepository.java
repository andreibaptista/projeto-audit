package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import java.util.Collection;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Integer> {

    @Query(value = "SELECT S.* FROM SIAAG.TAB_PONTO_ATENDIMENTO_SETOR PTS, SIAAG.TAB_SETOR S WHERE PTS.ID_SETOR = S.ID AND PTS.ID_PONTO_ATENDIMENTO = :idPontoAtendimento", nativeQuery = true)
    Collection<Setor> listarPorPontoAtendimentoId(@Param("idPontoAtendimento") Integer idPontoAtendimento);
}
