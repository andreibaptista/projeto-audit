package br.gov.pa.igeprev.siaag.service;

import java.util.Collection;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Perfil;

public interface IPerfilService {

	Perfil findById(int id) throws ServiceException;

	Collection<Perfil> findAll() throws ServiceException;

	Perfil salvar(Perfil Perfil) throws ServiceException;

	Perfil editar(Perfil Perfil) throws ServiceException;

	Integer excluir(int id) throws ServiceException;
}
