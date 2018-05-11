package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.PontoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PontoAtendimentoRepository extends JpaRepository<PontoAtendimento, Integer> {

}
