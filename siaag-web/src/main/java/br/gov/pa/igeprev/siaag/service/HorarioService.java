package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Horario;
import br.gov.pa.igeprev.siaag.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

/**
 * Classe de negócio da entidade Horario.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class HorarioService implements IHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Transactional(readOnly = true)
    public Horario findByIdAndAtivo(int id) throws ServiceException {
        return horarioRepository.findByIdAndAtivo(id, true);
    }

    @Transactional(readOnly = true)
    public Collection<Horario> findByAtivo() throws ServiceException {
        return horarioRepository.findByAtivo(true);
    }

    @Transactional(readOnly = true)
    public Collection<Horario> findHorarioInicioDisponivelByAgendaId(@NotNull Integer agendaId) throws ServiceException {
        return horarioRepository.findHorarioInicioDisponivelByAgendaId(agendaId);
    }

    @Transactional(readOnly = true)
    public Horario findByHorarioAndAtivo(Date horario, Boolean ativo) throws ServiceException {
        return horarioRepository.findByHorarioAndAtivo(horario, ativo);
    }

    @Transactional(readOnly = true)
    public Horario findPrimeiroHorarioAtivo() throws ServiceException {
        return horarioRepository.findPrimeiroHorarioAtivo();
    }

    @Transactional(readOnly = true)
    public Horario findUltimoHorarioAtivo() throws ServiceException {
        return horarioRepository.findUltimoHorarioAtivo();
    }



    @Transactional(readOnly = true)
    public Horario findByHorario(Date time) throws ServiceException {
        return horarioRepository.findByHorario(time);
    }
}
