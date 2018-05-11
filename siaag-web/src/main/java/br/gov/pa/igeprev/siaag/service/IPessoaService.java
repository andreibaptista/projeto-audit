package br.gov.pa.igeprev.siaag.service;

import java.util.Collection;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Pessoa;

public interface IPessoaService {

	Pessoa findById(int id) throws ServiceException;

	Collection<Pessoa> findAll() throws ServiceException;

	Collection<Pessoa> findByNome(String nome) throws ServiceException;

	Pessoa findByUsuario(int idUsuario) throws ServiceException;

	Pessoa findByCpf(String cpf) throws ServiceException;

	Pessoa findByEmail(String email) throws ServiceException;

	Pessoa salvar(Pessoa pessoa) throws ServiceException;

	Pessoa editar(Pessoa pessoa) throws ServiceException;

	Integer excluir(int id) throws ServiceException;
}
