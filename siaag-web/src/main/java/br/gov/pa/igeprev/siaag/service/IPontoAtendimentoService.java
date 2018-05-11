package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.PontoAtendimento;

import java.util.Collection;

public interface IPontoAtendimentoService {

    PontoAtendimento findById(int id) throws ServiceException;

    Collection<PontoAtendimento> findAll() throws ServiceException;

    PontoAtendimento salvar(PontoAtendimento pontoAtendimento) throws ServiceException;

    PontoAtendimento editar(PontoAtendimento pontoAtendimento) throws ServiceException;

    Integer excluir(int id) throws ServiceException;
}
