package br.gov.pa.igeprev.siaag.service.external;

import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.external.VwCadastroSiaag;

import java.util.Collection;

public interface IVwCadastroSiaagService {
    Collection<VwCadastroSiaag> findByCpf(String cpf) throws ServiceException;
}
