package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Agenda;
import br.gov.pa.igeprev.siaag.model.AgendaItem;
import br.gov.pa.igeprev.siaag.model.BloqueioAgenda;
import br.gov.pa.igeprev.siaag.repository.AgendaItemRepository;
import br.gov.pa.igeprev.siaag.repository.BloqueioAgendaRepository;
import br.gov.pa.igeprev.siaag.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Classe de negócio da entidade BloqueioBloqueioAgenda.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class BloqueioAgendaService implements IBloqueioAgendaService {
    @Autowired
    private BloqueioAgendaRepository bloqueioAgendaRepository;
    @Autowired
    private AgendaService agendaService;

    @Transactional(readOnly = true)
    public BloqueioAgenda findById(int id) throws ServiceException {
        return bloqueioAgendaRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<BloqueioAgenda> findAll() throws ServiceException {
        return bloqueioAgendaRepository.findAll();
    }

    @Transactional
    public BloqueioAgenda salvar(BloqueioAgenda bloqueioAgenda) throws ServiceException {
        List<Agenda> agendas = new ArrayList<>(agendaService.findByUsuarioIdAndDataBetween(bloqueioAgenda.getUsuario().getId(), bloqueioAgenda.getDataInicio(), bloqueioAgenda.getDataFim()));
        agendaService.excluir(agendas);
        return bloqueioAgendaRepository.save(bloqueioAgenda);
    }

    @Transactional
    public BloqueioAgenda editar(BloqueioAgenda BloqueioAgenda) throws ServiceException {
        Collection<BloqueioAgenda> BloqueioAgendas = findAll();
        for (BloqueioAgenda p : BloqueioAgendas) {
            if (p.getId().equals(BloqueioAgenda.getId())) {
                return bloqueioAgendaRepository.save(BloqueioAgenda);
            }
        }
        return null;
    }

    @Transactional
    public Integer excluir(int id) throws ServiceException {
        Collection<BloqueioAgenda> BloqueioAgendas = findAll();
        for (BloqueioAgenda p : BloqueioAgendas) {
            if (p.getId().equals(id)) {
                bloqueioAgendaRepository.delete(id);
                return id;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Collection<BloqueioAgenda> findByUsuarioId(@NotNull Integer id) throws ServiceException {
        return bloqueioAgendaRepository.findByUsuarioId(id);
    }

    @Transactional(readOnly = true)
    public Collection<BloqueioAgenda> findByUsuarioIdAndData(Integer usuarioId, Date data) throws ServiceException {
        return bloqueioAgendaRepository.findByUsuarioIdAndData(usuarioId, data);
    }

}
