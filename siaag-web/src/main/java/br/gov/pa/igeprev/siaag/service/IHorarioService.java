package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Horario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;

public interface IHorarioService {

    Horario findByIdAndAtivo(int id) throws ServiceException;

    Collection<Horario> findByAtivo() throws ServiceException;

    Horario findByHorarioAndAtivo(Date horario, Boolean ativo) throws ServiceException;

    Collection<Horario> findHorarioInicioDisponivelByAgendaId(Integer agendaId) throws ServiceException;

    Horario findPrimeiroHorarioAtivo() throws ServiceException;

    Horario findUltimoHorarioAtivo() throws ServiceException;

    Horario findByHorario(Date time) throws ServiceException;
}
