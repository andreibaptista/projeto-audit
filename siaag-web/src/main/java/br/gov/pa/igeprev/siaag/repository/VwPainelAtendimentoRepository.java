package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.model.view.VwPainelAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface VwPainelAtendimentoRepository extends JpaRepository<VwPainelAtendimento, Date> {
    Collection<VwPainelAtendimento> findByFormaAtendimento(FormaAtendimentoEnum formaAtendimento);

    Collection<VwPainelAtendimento> findByAtendenteIdAndFormaAtendimento(Integer idUsuario, FormaAtendimentoEnum formaAtendimento);
}
