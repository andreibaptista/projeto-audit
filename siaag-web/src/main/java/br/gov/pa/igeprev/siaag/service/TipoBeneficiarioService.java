package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.TipoBeneficiario;
import br.gov.pa.igeprev.siaag.repository.TipoBeneficiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TipoBeneficiarioService implements ITipoBeneficiarioService {

    @Autowired
    private TipoBeneficiarioRepository tipoBeneficiarioRepository;

    @Override
    public TipoBeneficiario findById(int id) throws ServiceException {
        return tipoBeneficiarioRepository.findOne(id);
    }

    @Override
    public Collection<TipoBeneficiario> findAll() throws ServiceException {
        return tipoBeneficiarioRepository.findAll();
    }
}
