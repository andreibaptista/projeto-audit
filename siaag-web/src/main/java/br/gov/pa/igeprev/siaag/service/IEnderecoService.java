package br.gov.pa.igeprev.siaag.service;

import java.util.Collection;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Endereco;

public interface IEnderecoService {

	Endereco findById(int id) throws ServiceException;

	Collection<Endereco> findAll() throws ServiceException;

	Endereco salvar(Endereco Endereco) throws ServiceException;

	Endereco editar(Endereco Endereco) throws ServiceException;

	Integer excluir(int id) throws ServiceException;
}
