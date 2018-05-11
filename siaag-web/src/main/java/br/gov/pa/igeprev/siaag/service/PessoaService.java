package br.gov.pa.igeprev.siaag.service;

import java.util.Collection;

import br.gov.pa.igeprev.siaag.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Pessoa;
import br.gov.pa.igeprev.siaag.repository.PessoaRepository;

/**
 * Classe de negócio da entidade Pessoa.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class PessoaService implements IPessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional(readOnly = true)
    public Pessoa findById(int id) throws ServiceException {
        return pessoaRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<Pessoa> findAll() throws ServiceException {
        return pessoaRepository.findAllByOrderByNome();
    }

    @Transactional(readOnly = true)
    public Pessoa findByUsuario(int idUsuario) throws ServiceException {
        return pessoaRepository.findByUsuario(idUsuario);
    }

    @Transactional(readOnly = true)
    public Collection<Pessoa> findByNome(String nome) throws ServiceException {
        return pessoaRepository.findByNomeContains(nome);
    }

    @Transactional(readOnly = true)
    public Pessoa findByCpf(String cpf) throws ServiceException {
        return pessoaRepository.findByCpf(cpf);
    }

    @Transactional(readOnly = true)
    public Pessoa findByEmail(String email) throws ServiceException {
        return pessoaRepository.findByEmail(email);
    }

    @Transactional
    public Pessoa salvar(Pessoa pessoa) throws ServiceException {
        if(pessoa.getEndereco() != null && (
                (pessoa.getEndereco().getCep() != null && !pessoa.getEndereco().getCep().isEmpty()) ||
                (pessoa.getEndereco().getBairro() != null && !pessoa.getEndereco().getBairro().isEmpty()) ||
                (pessoa.getEndereco().getCidade() != null && !pessoa.getEndereco().getCidade().isEmpty()) ||
                (pessoa.getEndereco().getLogradouro() != null && !pessoa.getEndereco().getLogradouro().isEmpty()) ||
                (pessoa.getEndereco().getComplemento() != null && !pessoa.getEndereco().getComplemento().isEmpty()) ||
                (pessoa.getEndereco().getNumero() != null && !pessoa.getEndereco().getNumero().isEmpty()) ||
                (pessoa.getEndereco().getUf() != null && !pessoa.getEndereco().getUf().isEmpty()))) {
            enderecoRepository.save(pessoa.getEndereco());
        }else{
            pessoa.setEndereco(null);
        }
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public Pessoa editar(Pessoa pessoa) throws ServiceException {
        Collection<Pessoa> pessoas = findAll();
        for (Pessoa p : pessoas) {
            if (p.getId().equals(pessoa.getId())) {
                return pessoaRepository.save(pessoa);
            }
        }
        return null;
    }

    @Transactional
    public Integer excluir(int id) throws ServiceException {
        Collection<Pessoa> pessoas = findAll();
        for (Pessoa p : pessoas) {
            if (p.getId().equals(id)) {
                pessoaRepository.delete(id);
                return id;
            }
        }
        return null;
    }

}
