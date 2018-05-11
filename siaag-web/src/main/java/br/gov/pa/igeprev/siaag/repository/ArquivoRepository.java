package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Classe reponsável por realizar as transações com o banco de dados da tabela Arquivo.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {
    Collection<Arquivo> findByAtendimentoId(Integer id);
}
