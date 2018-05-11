package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.TipoBeneficiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoBeneficiarioRepository extends JpaRepository<TipoBeneficiario, Integer> {
}
