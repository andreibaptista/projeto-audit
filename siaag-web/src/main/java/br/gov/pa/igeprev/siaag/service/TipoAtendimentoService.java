package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.TipoAtendimento;
import br.gov.pa.igeprev.siaag.repository.TipoAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Classe de negócio da entidade Tipo Atendimento.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 12/03/2018
 */
@Service
public class TipoAtendimentoService implements ITipoAtendimentoService {
    @Autowired
    private TipoAtendimentoRepository tipoAtendimentoRepository;

    @Transactional(readOnly = true)
    public TipoAtendimento findById(int id) throws ServiceException {
        return tipoAtendimentoRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<TipoAtendimento> findAll() throws ServiceException {
        return tipoAtendimentoRepository.findAll();
    }

    @Transactional
    public TipoAtendimento salvar(TipoAtendimento tipoAtendimento) throws ServiceException {
        return tipoAtendimentoRepository.save(tipoAtendimento);
    }

    @Transactional
    public TipoAtendimento editar(TipoAtendimento tipoAtendimento) throws ServiceException {
        Collection<TipoAtendimento> tipoAtendimentos = findAll();
        for (TipoAtendimento p : tipoAtendimentos) {
            if (p.getId().equals(tipoAtendimento.getId())) {
                return tipoAtendimentoRepository.save(tipoAtendimento);
            }
        }
        return null;
    }

    @Transactional
    public Integer excluir(int id) throws ServiceException {
        Collection<TipoAtendimento> tipoAtendimentos = findAll();
        for (TipoAtendimento p : tipoAtendimentos) {
            if (p.getId().equals(id)) {
                tipoAtendimentoRepository.delete(id);
                return id;
            }
        }
        return null;
    }
}
