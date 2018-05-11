package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Agenda;
import br.gov.pa.igeprev.siaag.model.Perfil;
import br.gov.pa.igeprev.siaag.model.Pessoa;
import br.gov.pa.igeprev.siaag.model.Usuario;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

public interface IUsuarioService {
    Usuario findById(int id) throws ServiceException;

    Collection<Usuario> findAll() throws ServiceException;

    Usuario findByLoginAndSenhaAndAtivo(String login, String senha, boolean ativo) throws ServiceException;

    Usuario findByLoginAndAtivo(String login, boolean ativo) throws ServiceException;

    Usuario findByLogin(String login) throws ServiceException;

    Usuario findByLoginAndSenha(String login, String senha) throws ServiceException;

    Usuario findByPessoa(Pessoa pessoa) throws ServiceException;

    Usuario salvar(Usuario usuario) throws ServiceException;

    Usuario editar(Usuario usuario) throws ServiceException;

    Integer excluir(int id) throws ServiceException;

    Usuario findByCpfAndNomeAndDataNascimento(String cpf, String nome, Date dataNascimento) throws ServiceException;

    Usuario findByCpfAndNomeAndDataNascimentoAndMatricula(String cpf, String nome, Date dataNascimento, String matricula) throws ServiceException;

    Collection<Usuario> findByPerfilId(Integer id);

    Collection<Usuario> findByPerfilIdAndPessoaNome(@NotNull Integer idPerfil, @NotNull String nome) throws ServiceException;

    Collection<Usuario> findByPessoaNome(String pessoaNome) throws ServiceException;
}
