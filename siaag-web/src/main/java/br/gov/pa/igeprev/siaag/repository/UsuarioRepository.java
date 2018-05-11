package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.model.Pessoa;
import br.gov.pa.igeprev.siaag.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByLogin(String login);

    Usuario findByLoginAndSenhaAndAtivo(String login, String senha, boolean ativo);

    Usuario findByLoginAndAtivo(String login, boolean ativo);

    Usuario findByLoginAndSenha(String login, String senha);

    Usuario findByPessoaOrderByPessoaNome(Pessoa pessoa);

    Collection<Usuario> findByPessoaNomeContainsIgnoreCaseOrderByPessoaNome(String pessoaNome);

    @Query(value = "SELECT u.* FROM tab_usuario u JOIN tab_pessoa p ON u.id_pessoa = p.id WHERE u.login =:cpf AND lower(p.nome) = lower(:nome) AND p.data_nascimento = :dataNascimento", nativeQuery = true)
    Usuario findByCpfAndNomeAndDataNascimento(@Param(value = "cpf") String cpf, @Param(value = "nome") String nome, @Param(value = "dataNascimento") Date dataNascimento);

    @Query(value = "SELECT u.* FROM tab_usuario u JOIN tab_pessoa p ON u.id_pessoa = p.id WHERE u.login =:cpf AND lower(p.nome) = lower(:nome) AND p.data_nascimento = :dataNascimento AND UPPER(u.matricula) = UPPER(:matricula)", nativeQuery = true)
    Usuario findByCpfAndNomeAndDataNascimentoAndMatricula(@Param(value = "cpf") String cpf, @Param(value = "nome") String nome, @Param(value = "dataNascimento") Date dataNascimento, @Param("matricula") String matricula);

    Collection<Usuario> findByPerfilIdOrderByPessoaNome(Integer id);

    Collection<Usuario> findByPerfilIdAndPessoaNomeContainsIgnoreCaseOrderByPessoaNome(Integer idPerfil, String nome);

}
