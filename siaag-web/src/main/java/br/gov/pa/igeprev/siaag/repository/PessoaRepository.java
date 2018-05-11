package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
    Pessoa findByCpf(String cpf);

    Pessoa findByEmail(String email);

    @Query(value = "SELECT p.* FROM tab_pessoa p JOIN tab_usuario u ON p.id = u.id_pessoa WHERE u.id = ?1", nativeQuery = true)
    Pessoa findByUsuario(Integer idUsuario);

    Collection<Pessoa> findByNomeContains(String nome);

    Collection<Pessoa> findAllByOrderByNome();

}
