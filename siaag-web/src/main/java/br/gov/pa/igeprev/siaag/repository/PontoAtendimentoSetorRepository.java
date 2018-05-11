package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.PontoAtendimentoSetor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PontoAtendimentoSetorRepository extends JpaRepository<PontoAtendimentoSetor, Integer> {
    PontoAtendimentoSetor findByPontoAtendimentoIdAndSetorId(Integer idPontoAtendimento, Integer idSetor);
}