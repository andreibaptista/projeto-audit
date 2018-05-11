package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.PontoAtendimento;
import br.gov.pa.igeprev.siaag.repository.PontoAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Classe de negócio da entidade PontoAtendimento.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class PontoAtendimentoService implements IPontoAtendimentoService {
    @Autowired
    private PontoAtendimentoRepository pontoAtendimentoRepository;

    @Transactional(readOnly = true)
    public PontoAtendimento findById(int id) throws ServiceException {
        return pontoAtendimentoRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<PontoAtendimento> findAll() throws ServiceException {
        return pontoAtendimentoRepository.findAll();
    }

    @Transactional
    public PontoAtendimento salvar(PontoAtendimento pontoAtendimento) throws ServiceException {
        return pontoAtendimentoRepository.save(pontoAtendimento);
    }

    @Transactional
    public PontoAtendimento editar(PontoAtendimento PontoAtendimento) throws ServiceException {
        Collection<PontoAtendimento> PontoAtendimentos = findAll();
        for (PontoAtendimento p : PontoAtendimentos) {
            if (p.getId().equals(PontoAtendimento.getId())) {
                return pontoAtendimentoRepository.save(PontoAtendimento);
            }
        }
        return null;
    }

    @Transactional
    public Integer excluir(int id) throws ServiceException {
        Collection<PontoAtendimento> PontoAtendimentos = findAll();
        for (PontoAtendimento p : PontoAtendimentos) {
            if (p.getId().equals(id)) {
                pontoAtendimentoRepository.delete(id);
                return id;
            }
        }
        return null;
    }

}
