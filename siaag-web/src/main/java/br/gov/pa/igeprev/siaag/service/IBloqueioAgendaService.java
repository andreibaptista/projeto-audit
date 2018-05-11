package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.BloqueioAgenda;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

public interface IBloqueioAgendaService {

    BloqueioAgenda findById(int id) throws ServiceException;

    Collection<BloqueioAgenda> findAll() throws ServiceException;

    BloqueioAgenda salvar(BloqueioAgenda bloqueioAgenda) throws ServiceException;

    BloqueioAgenda editar(BloqueioAgenda bloqueioAgenda) throws ServiceException;

    Integer excluir(int id) throws ServiceException;

    Collection<BloqueioAgenda> findByUsuarioId(@NotNull Integer id) throws ServiceException;

    Collection<BloqueioAgenda> findByUsuarioIdAndData(Integer usuarioId, Date data) throws ServiceException;
}
