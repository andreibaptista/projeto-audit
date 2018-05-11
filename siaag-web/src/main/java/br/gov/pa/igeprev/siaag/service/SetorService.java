package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Setor;
import br.gov.pa.igeprev.siaag.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class SetorService implements ISetorService {
    @Autowired
    private SetorRepository setorRepository;

    @Transactional(readOnly = true)
    public Setor findById(int id) throws ServiceException {
        return setorRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<Setor> findAll() throws ServiceException {
        return setorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<Setor> findByPontoAtendimentoId(Integer idPontoAtendimento) throws ServiceException {
        return setorRepository.listarPorPontoAtendimentoId(idPontoAtendimento);
    }
}
