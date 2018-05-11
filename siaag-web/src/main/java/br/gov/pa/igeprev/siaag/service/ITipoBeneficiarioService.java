package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.TipoBeneficiario;

import java.util.Collection;

public interface ITipoBeneficiarioService {

    TipoBeneficiario findById(int id) throws ServiceException;

    Collection<TipoBeneficiario> findAll() throws ServiceException;

}
