package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.AgendaItem;
import br.gov.pa.igeprev.siaag.model.BloqueioAgenda;
import br.gov.pa.igeprev.siaag.repository.AgendaItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Classe de negócio da entidade AgendaItem.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class AgendaItemService implements IAgendaItemService {
    @Autowired
    private AgendaItemRepository agendaItemRepository;
    @Autowired
    private BloqueioAgendaService bloqueioAgendaService;

    @Transactional(readOnly = true)
    public AgendaItem findById(int id) throws ServiceException {
        return agendaItemRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> findAll() throws ServiceException {
        return agendaItemRepository.findAll();
    }

    @Transactional
    public AgendaItem salvar(AgendaItem agendaItem) throws ServiceException {
        return agendaItemRepository.save(agendaItem);
    }

    @Transactional
    public void salvar(List<AgendaItem> agendaItems) throws ServiceException {
        agendaItemRepository.save(agendaItems);
    }

    @Transactional
    public AgendaItem editar(AgendaItem agendaItem) throws ServiceException {
        Collection<AgendaItem> AgendaItems = findAll();
        for (AgendaItem p : AgendaItems) {
            if (p.getId().equals(agendaItem.getId())) {
                return agendaItemRepository.save(agendaItem);
            }
        }
        return null;
    }

    @Transactional
    public Integer excluir(int id) throws ServiceException {
        Collection<AgendaItem> agendaItems = findAll();
        for (AgendaItem p : agendaItems) {
            if (p.getId().equals(id)) {
                agendaItemRepository.delete(id);
                return id;
            }
        }
        return null;
    }

    @Transactional
    public void excluir(Collection<AgendaItem> agendaItems) throws ServiceException {
        agendaItemRepository.delete(agendaItems);
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> findByAgendaId(@NotNull Integer id) throws ServiceException {
        return agendaItemRepository.findByAgendaId(id);
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> listarTodasAtivasSemAtendimentoDataAcimaData(Date data, String formaAtendimento) throws ServiceException {
        return agendaItemRepository.listarTodasAtivasSemAtendimentoDataAcimaData(data, formaAtendimento);
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> listarTodasDisponiveisAgendamento(Date data, String formaAtendimento) throws ServiceException {
        Collection<AgendaItem> agendaItemsDisponiveis = new ArrayList<>();
        List<AgendaItem> agendaItems = new ArrayList<>(listarTodasAtivasSemAtendimentoDataAcimaData(data, formaAtendimento));
        for (AgendaItem agendaItem : agendaItems) {
            List<BloqueioAgenda> bloqueioAgendas = new ArrayList<>(bloqueioAgendaService.findByUsuarioId(agendaItem.getAgenda().getUsuario().getId()));
            if (!bloqueioAgendas.isEmpty()) {
                for (BloqueioAgenda bloqueioAgenda : bloqueioAgendas) {
                    if (agendaItem.getAgenda().getData().compareTo(bloqueioAgenda.getDataInicio()) >= 0 &&
                            agendaItem.getAgenda().getData().compareTo(bloqueioAgenda.getDataFim()) <= 0) {
                        if (!(agendaItem.getHorarioInicio().getId() >= bloqueioAgenda.getHorarioInicio().getId() &&
                                agendaItem.getHorarioFim().getId() <= bloqueioAgenda.getHorarioFim().getId())) {
                            agendaItemsDisponiveis.add(agendaItem);
                        }
                    } else {
                        agendaItemsDisponiveis.add(agendaItem);
                    }
                }
            } else {
                agendaItemsDisponiveis.add(agendaItem);
            }
        }
        return agendaItemsDisponiveis;
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> listarAgendaItemDisponivelPorDataHorarioAndFormaAtendimento(Date data, Integer horario, FormaAtendimentoEnum formaAtendimentoEnum) throws ServiceException {
        return agendaItemRepository.listarAgendaItemDisponivelPorDataHorarioAndFormaAtendimento(data, horario, formaAtendimentoEnum.getValue());
    }

    @Transactional(readOnly = true)
    public AgendaItem findByAgendaIdAndId(Integer agendaId, Integer agendaItemId) throws ServiceException {
        return agendaItemRepository.findByAgendaIdAndId(agendaId, agendaItemId);
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> findByAgendaHorarioInicioAndHorarioFim(Integer agendaId, Integer horarioInicioId, Integer horarioFimId) throws ServiceException {
        return agendaItemRepository.findByAgendaHorarioInicioAndHorarioFim(agendaId, horarioInicioId, horarioFimId);
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> findByAgendaAtendenteIdHorarioInicioIdAndHorarioFimIdAndAgendaDataBetween(Integer atendenteId, Integer horarioInicioId, Integer horarioFimId, Date dataInicio, Date dataFim) throws ServiceException {
        return agendaItemRepository.findByAgendaUsuarioIdAndHorarioInicioIdAndHorarioFimIdAndAgendaDataBetween(atendenteId, horarioInicioId, horarioFimId, dataInicio, dataFim);
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> findByAgendaUsuarioIdAndAgendaDataBetween(Integer usuarioId, Date dataInicio, Date dataFim) throws ServiceException {
        return agendaItemRepository.findByAgendaUsuarioIdAndAgendaDataBetween(usuarioId, dataInicio, dataFim);
    }

    @Transactional(readOnly = true)
    public Collection<AgendaItem> findByAgendaUsuarioIdAndAgendaData(Integer usuarioId, Date data) throws ServiceException {
        return agendaItemRepository.findByAgendaUsuarioIdAndAgendaData(usuarioId, data);
    }
}
