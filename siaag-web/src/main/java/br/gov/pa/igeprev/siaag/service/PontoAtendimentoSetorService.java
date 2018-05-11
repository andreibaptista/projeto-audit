package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.PontoAtendimentoSetor;
import br.gov.pa.igeprev.siaag.repository.PontoAtendimentoSetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PontoAtendimentoSetorService implements IPontoAtendimentoSetorService {
    @Autowired
    private PontoAtendimentoSetorRepository pontoAtendimentoSetorRepository;

    @Override
    public Collection<PontoAtendimentoSetor> findAll() throws ServiceException {
        return pontoAtendimentoSetorRepository.findAll();
    }

    @Override
    public PontoAtendimentoSetor findByPontoAtendimentoIdAndSetorId(Integer idPontoAtendimento, Integer idSetor) {
        return pontoAtendimentoSetorRepository.findByPontoAtendimentoIdAndSetorId(idPontoAtendimento, idSetor);
    }
}
