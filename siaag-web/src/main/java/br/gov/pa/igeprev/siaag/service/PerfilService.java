package br.gov.pa.igeprev.siaag.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Perfil;
import br.gov.pa.igeprev.siaag.repository.PerfilRepository;

/**
* Classe de negócio da entidade Perfil.
*
* @author  José Aleixo Araujo Porpino Filho
* @version 1.0
* @since   19/01/2018 
*/
@Service
public class PerfilService implements IPerfilService {
	@Autowired
	private PerfilRepository perfilRepository;

	@Transactional(readOnly = true)
	public Perfil findById(int id) throws ServiceException {
		return perfilRepository.findOne(id);
	}

	@Transactional(readOnly = true)
	public Collection<Perfil> findAll() throws ServiceException {
		return perfilRepository.findAllByOrderByDescricao();
	}
	
	@Transactional
	public Perfil salvar(Perfil Perfil) throws ServiceException {
		return perfilRepository.save(Perfil);
	}

	@Transactional
	public Perfil editar(Perfil Perfil) throws ServiceException {
		Collection<Perfil> Perfils = findAll();
		for (Perfil p : Perfils) {
			if (p.getId().equals(Perfil.getId())) {
				return perfilRepository.save(Perfil);
			}
		}
		return null;
	}

	@Transactional
	public Integer excluir(int id) throws ServiceException {
		Collection<Perfil> Perfils = findAll();
		for (Perfil p : Perfils) {
			if (p.getId().equals(id)) {
				perfilRepository.delete(id);
				return id;
			}
		}
		return null;
	}

}
