package br.gov.pa.igeprev.siaag.service.external;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Agenda;
import br.gov.pa.igeprev.siaag.model.AgendaItem;
import br.gov.pa.igeprev.siaag.model.external.VwCadastroSiaag;
import br.gov.pa.igeprev.siaag.repository.AgendaRepository;
import br.gov.pa.igeprev.siaag.repository.external.VwCadastroSiaagRepository;
import br.gov.pa.igeprev.siaag.service.AgendaItemService;
import br.gov.pa.igeprev.siaag.service.IAgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

/**
 * Classe de negócio da entidade VwCadastroSiaag.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class VwCadastroSiaagService implements IVwCadastroSiaagService {
    @Autowired
    private VwCadastroSiaagRepository vwCadastroSiaagRepository;

    @Transactional(readOnly = true)
    public Collection<VwCadastroSiaag> findByCpf(String cpf) throws ServiceException {
        return vwCadastroSiaagRepository.findByCpf(cpf);
    }
}
