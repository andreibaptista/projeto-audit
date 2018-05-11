package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.PontoAtendimentoSetor;

import java.util.Collection;

public interface IPontoAtendimentoSetorService {

    Collection<PontoAtendimentoSetor> findAll() throws ServiceException;

    PontoAtendimentoSetor findByPontoAtendimentoIdAndSetorId(Integer idPontoAtendimento, Integer idSetor);
}
