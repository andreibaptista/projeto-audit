package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.enumeration.PerfilEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Atendimento;
import br.gov.pa.igeprev.siaag.model.Pessoa;
import br.gov.pa.igeprev.siaag.model.Usuario;
import br.gov.pa.igeprev.siaag.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

/**
 * Classe de negócio da entidade Usuario.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private AtendimentoService atendimentoService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional(readOnly = true)
    public Usuario findById(int id) throws ServiceException {
        return usuarioRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<Usuario> findAll() throws ServiceException {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findByPessoa(Pessoa pessoa) throws ServiceException {
        return usuarioRepository.findByPessoaOrderByPessoaNome(pessoa);
    }

    @Transactional(readOnly = true)
    public Usuario findByLoginAndSenhaAndAtivo(String login, String senha, boolean ativo) throws ServiceException {
        return usuarioRepository.findByLoginAndSenhaAndAtivo(login, senha, ativo);
    }

    @Transactional(readOnly = true)
    public Usuario findByLoginAndAtivo(String login, boolean ativo) throws ServiceException {
        return usuarioRepository.findByLoginAndAtivo(login, ativo);
    }

    @Transactional(readOnly = true)
    public Usuario findByLogin(String login) throws ServiceException {
        return usuarioRepository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public Usuario findByLoginAndSenha(String login, String senha) throws ServiceException {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    @Transactional
    public Usuario salvar(Usuario usuario) throws ServiceException {
        usuario.setLogin(usuario.getPessoa().getCpf());
        if (!usuarioMesmoLogin(usuario)) {
            Pessoa pessoaExistente = pessoaService.findByCpf(usuario.getPessoa().getCpf());
            if(pessoaExistente != null && pessoaExistente.getId() != null) usuario.getPessoa().setId(pessoaExistente.getId());
            usuario.setLogin(usuario.getPessoa().getCpf());
            usuario.setPessoa(pessoaService.salvar(usuario.getPessoa()));
            if (usuario.getId() == null || usuario.getSenha().length() < 50) {
                usuario.setSenha(encoder.encode(usuario.getSenha()));
            }
            return usuarioRepository.save(usuario);
        } else {
            throw new ServiceException("mensagemERROCpfExistente");
        }
    }

    @Transactional
    public Usuario editar(Usuario usuario) throws ServiceException {
        usuario.setLogin(usuario.getPessoa().getCpf());
        if (!usuarioMesmoLogin(usuario)) {
            Collection<Usuario> usuarios = findAll();
            for (Usuario u : usuarios) {
                if (u.getId().equals(usuario.getId())) {
                    usuario.setLogin(usuario.getPessoa().getCpf());
                    return usuarioRepository.save(usuario);
                }
            }
            return null;
        } else {
            throw new ServiceException("mensagemERROCpfExistente");
        }
    }

    @Transactional
    public Integer excluir(@NotNull int id) throws ServiceException {
        Collection<Usuario> usuarios = findAll();
        for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                if (u.getPerfil().getId() == PerfilEnum.ATENDENTE.getId() ||
                        u.getPerfil().getId() == PerfilEnum.ADMINISTRADOR.getId()) {
                    Collection<Atendimento> atendimentos = atendimentoService.findByAtendenteId(u.getPessoa().getId());
                    if (atendimentos != null && atendimentos.size() > 0) {
                        throw new ServiceException("mensagemErroUsuarioAtendimentoExistente");
                    } else {
                        usuarioRepository.delete(id);
                    }
                }else{
                    usuarioRepository.delete(id);
                }
                return id;
            }
        }
        return null;
    }

    public Boolean usuarioMesmoLogin(@NotNull Usuario usuario) throws ServiceException {
        Usuario usuarioExistente = findByLogin(usuario.getLogin());
        if (usuarioExistente != null && !usuarioExistente.getId().equals(usuario.getId())) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Usuario findByCpfAndNomeAndDataNascimento(@NotNull String cpf, @NotNull String nome, @NotNull Date dataNascimento) throws ServiceException {
        return usuarioRepository.findByCpfAndNomeAndDataNascimento(cpf, nome, dataNascimento);
    }

    @Transactional(readOnly = true)
    public Usuario findByCpfAndNomeAndDataNascimentoAndMatricula(@Param(value = "cpf") String cpf, @Param(value = "nome") String nome, @Param(value = "dataNascimento") Date dataNascimento, @Param("matricula") String matricula) {
        return usuarioRepository.findByCpfAndNomeAndDataNascimentoAndMatricula(cpf, nome, dataNascimento, matricula);
    }

    @Transactional(readOnly = true)
    public Collection<Usuario> findByPerfilId(@NotNull Integer id) {
        return usuarioRepository.findByPerfilIdOrderByPessoaNome(id);
    }

    @Transactional(readOnly = true)
    public Collection<Usuario> findByPerfilIdAndPessoaNome(@NotNull Integer idPerfil, @NotNull String nome) throws ServiceException {
        return usuarioRepository.findByPerfilIdAndPessoaNomeContainsIgnoreCaseOrderByPessoaNome(idPerfil, nome);
    }

    @Transactional(readOnly = true)
    public Collection<Usuario> findByPessoaNome(String pessoaNome) throws ServiceException {
        return usuarioRepository.findByPessoaNomeContainsIgnoreCaseOrderByPessoaNome(pessoaNome);
    }

}
