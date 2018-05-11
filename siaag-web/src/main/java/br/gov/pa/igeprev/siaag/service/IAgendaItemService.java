package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.AgendaItem;
import br.gov.pa.igeprev.siaag.model.Horario;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

public interface IAgendaItemService {

    AgendaItem findById(int id) throws ServiceException;

    Collection<AgendaItem> findAll() throws ServiceException;

    AgendaItem salvar(AgendaItem agendaItem) throws ServiceException;

    AgendaItem editar(AgendaItem agendaItem) throws ServiceException;

    Integer excluir(int id) throws ServiceException;

    Collection<AgendaItem> findByAgendaId(@NotNull Integer id) throws ServiceException;

    Collection<AgendaItem> listarTodasAtivasSemAtendimentoDataAcimaData(Date data, String tipoAtendimento) throws ServiceException;

    Collection<AgendaItem> listarTodasDisponiveisAgendamento(Date data, String tipoAtendimento) throws ServiceException;

    Collection<AgendaItem> listarAgendaItemDisponivelPorDataHorarioAndFormaAtendimento(Date data, Integer horario, FormaAtendimentoEnum formaAtendimentoEnum) throws ServiceException;

    AgendaItem findByAgendaIdAndId(Integer agendaId, Integer agendaItemId) throws ServiceException;

    Collection<AgendaItem> findByAgendaAtendenteIdHorarioInicioIdAndHorarioFimIdAndAgendaDataBetween(Integer atendenteId, Integer horarioInicioId, Integer horarioFimId, Date dataInicio, Date dataFim) throws ServiceException;

    Collection<AgendaItem> findByAgendaUsuarioIdAndAgendaDataBetween(Integer usuarioId, Date dataInicio, Date dataFim) throws ServiceException;

    Collection<AgendaItem> findByAgendaUsuarioIdAndAgendaData(Integer usuarioId, Date data) throws ServiceException;

}
