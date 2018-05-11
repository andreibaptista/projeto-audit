package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Horario;
import br.gov.pa.igeprev.siaag.model.TipoAtendimento;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ITipoAtendimentoService {

    TipoAtendimento findById(int id) throws ServiceException;

    Collection<TipoAtendimento> findAll() throws ServiceException;

    TipoAtendimento salvar(TipoAtendimento tipoAtendimento) throws ServiceException;

    TipoAtendimento editar(TipoAtendimento tipoAtendimento) throws ServiceException;

    Integer excluir(int id) throws ServiceException;

}
