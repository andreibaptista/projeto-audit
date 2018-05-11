package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.SituacaoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SituacaoAtendimentoRepository extends JpaRepository<SituacaoAtendimento, Integer> {
}
