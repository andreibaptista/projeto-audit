package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.Horario;
import br.gov.pa.igeprev.siaag.model.TipoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Classe reponsável por realizar as transações com o banco de dados da tabela TipoAtendimento.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 12/03/2018
 */
@Repository
public interface TipoAtendimentoRepository extends JpaRepository<TipoAtendimento, Integer> {

}
