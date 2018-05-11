package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Setor;

import java.util.Collection;

public interface ISetorService {
    Setor findById(int id) throws ServiceException;

    Collection<Setor> findAll() throws ServiceException;

    Collection<Setor> findByPontoAtendimentoId(Integer idPontoAtendimento) throws ServiceException;
}
