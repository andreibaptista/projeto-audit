package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.model.view.VwDatasDisponiveis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;

public interface VwDatasDisponiveisRepository extends JpaRepository<VwDatasDisponiveis, Date> {

    Collection<VwDatasDisponiveis> findByDataAndFormaAtendimento(Date data, FormaAtendimentoEnum formaAtendimentoEnum);
    Collection<VwDatasDisponiveis> findByFormaAtendimento(FormaAtendimentoEnum formaAtendimento);
}
