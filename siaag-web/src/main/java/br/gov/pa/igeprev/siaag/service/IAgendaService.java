package br.gov.pa.igeprev.siaag.service;

import java.util.Collection;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Agenda;
import br.gov.pa.igeprev.siaag.model.AgendaItem;

public interface IAgendaService {

    Agenda findById(int id) throws ServiceException;

    Collection<Agenda> findAll() throws ServiceException;

    String salvar(Collection<Agenda> Agenda, Collection<AgendaItem> agendaItemsExclusao) throws ServiceException;

    Agenda editar(Agenda Agenda) throws ServiceException;

    Integer excluir(int id) throws ServiceException;

    Collection<Agenda> findByUsuarioId(@NotNull Integer id) throws ServiceException;

    Agenda findbyUsuarioIdAndData(Integer id, Date data) throws ServiceException;

    Collection<Agenda> listarDatasDisponiveisPorTipoAgendamento(FormaAtendimentoEnum formaAtendimento) throws ServiceException;

    Collection<Agenda> findByUsuarioIdAndDataBetween(Integer id, Date dataInicial, Date dataFinal) throws ServiceException;

    Collection<Agenda> findByDataAndAtendimentoNaoAgendadoAndUsuarioId(Date data, Boolean atendimentoNaoAgendado, Integer usuarioId) throws ServiceException;

    void removeByUsuarioId(Integer usuarioId) throws ServiceException;
}
