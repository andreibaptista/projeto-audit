package br.gov.pa.igeprev.siaag.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.model.Atendimento;
import br.gov.pa.igeprev.siaag.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Agenda;
import br.gov.pa.igeprev.siaag.model.AgendaItem;
import br.gov.pa.igeprev.siaag.repository.AgendaRepository;

/**
 * Classe de negócio da entidade Agenda.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class AgendaService implements IAgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private AgendaItemService agendaItemService;
    @Autowired
    private AtendimentoService atendimentoService;

    @Transactional(readOnly = true)
    public Agenda findById(int id) throws ServiceException {
        return agendaRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<Agenda> findAll() throws ServiceException {
        return agendaRepository.findAll();
    }

    @Transactional
    public String salvar(Collection<Agenda> agendas, Collection<AgendaItem> agendaItemsExclusao) throws ServiceException {
        StringBuilder mensagemRetorno = new StringBuilder();
        List<AgendaItem> agendaItems = new ArrayList<>();
        try {
            if (agendaItemsExclusao != null && !agendaItemsExclusao.isEmpty()) {
                agendaItemService.excluir(agendaItemsExclusao);
            }

            for (Agenda agenda : agendas) {
                Agenda agendaExistente = findbyUsuarioIdAndData(agenda.getUsuario().getId(), DataUtils.SomenteData(agenda.getData()));
                if (agendaExistente != null && agendaExistente.getId() != null) agenda.setId(agendaExistente.getId());
                agendaRepository.save(agenda);

                if (agenda.getAgendaItems() == null || (agenda.getAgendaItems() != null && agenda.getAgendaItems().size() == 0)
                        && agenda.getId() != null) {
                    Collection<Atendimento> atendimentosExistentes = atendimentoService.findByAtendenteIdAndDataAndSituacaoAtendimentoNotAgendado(agenda.getUsuario().getPessoa().getId(), DataUtils.SomenteData(agenda.getData()));
                    Collection<AgendaItem> agendaItemsExistentes = agendaItemService.findByAgendaId(agenda.getId());
                    if(atendimentosExistentes == null || atendimentosExistentes.isEmpty()){
                        agendaItemService.excluir(agendaItemsExistentes);
                    }
                }
                for (AgendaItem ai : agenda.getAgendaItems()) {
                    if (agendaExistente != null && agendaExistente.getId() != null) {
                        Collection<Atendimento> atendimentosExistentes = atendimentoService.findByAtendenteIdAndDataAndSituacaoAtendimentoNotAgendado(agenda.getUsuario().getPessoa().getId(), DataUtils.SomenteData(agenda.getData()));
                        Collection<AgendaItem> agendaItemsExistentes = agendaItemService.findByAgendaUsuarioIdAndAgendaData(agenda.getUsuario().getId(), DataUtils.SomenteData(agenda.getData()));
                        if (atendimentosExistentes == null || atendimentosExistentes.isEmpty()) {
                            agendaItemService.excluir(agendaItemsExistentes);
                            AgendaItem agendaItem = new AgendaItem();
                            agendaItem.setAgenda(agenda);
                            agendaItem.setAtivo(true);
                            agendaItem.setHorarioFim(ai.getHorarioFim());
                            agendaItem.setHorarioInicio(ai.getHorarioInicio());
                            agendaItem.setTipoAgendamento(ai.getTipoAgendamento());
                            agendaItems.add(agendaItem);
                        } else {
                            for (Atendimento atendExistente : atendimentosExistentes) {
                                if (!mensagemRetorno.toString().contains(atendExistente.getAtendente().getNome())) {
                                    mensagemRetorno.append(atendExistente.getAtendente().getNome()).append(", ");
                                }
                            }
                        }
                    } else {
                        AgendaItem agendaItem = new AgendaItem();
                        agendaItem.setAgenda(agenda);
                        agendaItem.setAtivo(true);
                        agendaItem.setHorarioFim(ai.getHorarioFim());
                        agendaItem.setHorarioInicio(ai.getHorarioInicio());
                        agendaItem.setTipoAgendamento(ai.getTipoAgendamento());
                        agendaItems.add(agendaItem);
                    }
                }

            }
            agendaItemService.salvar(agendaItems);
            if (!mensagemRetorno.toString().isEmpty())
                mensagemRetorno.replace(mensagemRetorno.length() - 2, mensagemRetorno.length(), ".");
            return mensagemRetorno.toString();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional
    public Agenda editar(Agenda Agenda) throws ServiceException {
        Collection<Agenda> Agendas = findAll();
        for (Agenda p : Agendas) {
            if (p.getId().equals(Agenda.getId())) {
                return agendaRepository.save(Agenda);
            }
        }
        return null;
    }

    @Transactional
    public Integer excluir(int id) throws ServiceException {
        Collection<Agenda> Agendas = findAll();
        for (Agenda p : Agendas) {
            if (p.getId().equals(id)) {
                agendaRepository.delete(id);
                return id;
            }
        }
        return null;
    }

    @Transactional
    public void excluir(Collection<Agenda> agendas) throws ServiceException {
        agendaRepository.delete(agendas);
    }

    @Transactional(readOnly = true)
    public Collection<Agenda> findByUsuarioId(@NotNull Integer id) throws ServiceException {
        return agendaRepository.findByUsuarioId(id);
    }

    @Transactional(readOnly = true)
    public Agenda findbyUsuarioIdAndData(Integer id, Date data) throws ServiceException {
        return agendaRepository.findByUsuarioIdAndData(id, data);
    }

    @Transactional(readOnly = true)
    public Collection<Agenda> listarDatasDisponiveisPorTipoAgendamento(FormaAtendimentoEnum formaAtendimento) throws ServiceException {
        return agendaRepository.listarDatasDisponiveisPorTipoAgendamento(formaAtendimento.getValue());
    }

    @Transactional(readOnly = true)
    public Collection<Agenda> findByUsuarioIdAndDataBetween(Integer id, Date dataInicial, Date dataFinal) throws ServiceException {
        return agendaRepository.findByUsuarioIdAndDataBetween(id, dataInicial, dataFinal);
    }

    @Transactional(readOnly = true)
    public Collection<Agenda> findByDataAndAtendimentoNaoAgendadoAndUsuarioId(Date data, Boolean atendimentoNaoAgendado, Integer usuarioId) throws ServiceException {
        return agendaRepository.findByDataAndAtendimentoNaoAgendadoAndUsuarioId(data, atendimentoNaoAgendado, usuarioId);
    }

    @Transactional
    public void removeByUsuarioId(Integer usuarioId) throws ServiceException {
        agendaRepository.removeAllByUsuarioId(usuarioId);
    }

}
