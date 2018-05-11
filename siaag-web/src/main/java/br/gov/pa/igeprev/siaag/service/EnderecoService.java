package br.gov.pa.igeprev.siaag.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Endereco;
import br.gov.pa.igeprev.siaag.repository.EnderecoRepository;

/**
* Classe de negócio da entidade Endereco.
*
* @author  José Aleixo Araujo Porpino Filho
* @version 1.0
* @since   19/01/2018 
*/
@Service
public class EnderecoService implements IEnderecoService {
	@Autowired
	private EnderecoRepository EnderecoRepository;

	@Transactional(readOnly = true)
	public Endereco findById(int id) throws ServiceException {
		return EnderecoRepository.findOne(id);
	}

	@Transactional(readOnly = true)
	public Collection<Endereco> findAll() throws ServiceException {
		return EnderecoRepository.findAll();
	}
	
	@Transactional
	public Endereco salvar(Endereco Endereco) throws ServiceException {
		return EnderecoRepository.save(Endereco);
	}

	@Transactional
	public Endereco editar(Endereco Endereco) throws ServiceException {
		Collection<Endereco> Enderecos = findAll();
		for (Endereco p : Enderecos) {
			if (p.getId().equals(Endereco.getId())) {
				return EnderecoRepository.save(Endereco);
			}
		}
		return null;
	}

	@Transactional
	public Integer excluir(int id) throws ServiceException {
		Collection<Endereco> Enderecos = findAll();
		for (Endereco p : Enderecos) {
			if (p.getId().equals(id)) {
				EnderecoRepository.delete(id);
				return id;
			}
		}
		return null;
	}

}
