package br.gov.pa.igeprev.siaag.repository.external;

import br.gov.pa.igeprev.siaag.model.external.VwCadastroSiaag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface VwCadastroSiaagRepository extends JpaRepository<VwCadastroSiaag, String> {
    @Query(name = "SELECT * " +
            "FROM VW_CADASTRO_SIAAG vw " +
            "WHERE vw.cpf IN ( " +
            "  SELECT " +
            "    cpf " +
            "  FROM (SELECT " +
            "          CPF, " +
            "          COUNT(NOME_PESSOA) qtd " +
            "        FROM VW_CADASTRO_SIAAG " +
            "        GROUP BY CPF) tb " +
            "  WHERE qtd = 1 " +
            ") AND (vw.TIPO_PESSOA = 'INATIVO' OR vw.TIPO_PESSOA like 'PENSIONISTA%') AND vw.cpf = :cpf " +
            "ORDER BY CPF", nativeQuery = true)
    Collection<VwCadastroSiaag> findByCpf(@Param("cpf") String cpf);
}
