package br.gov.pa.igeprev.siaag.repository;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Atendimento;
import br.gov.pa.igeprev.siaag.model.Horario;
import br.gov.pa.igeprev.siaag.model.Pessoa;
import br.gov.pa.igeprev.siaag.model.SituacaoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Integer> {
    /**
     * Busca atendimento existente para validação na hora de salvar o atendimento.
     *
     * @param ativo             se o atendimento esta ativo ou não
     * @param idBeneficiario    id do beneficiário
     * @param idTipoAtendimento id do tipo de atendimento
     * @param data              data do atendimento
     * @return Atendimento
     */
    Atendimento findByAtivoAndBeneficiarioIdAndTipoAtendimentoIdAndData(Boolean ativo, Integer idBeneficiario, Integer idTipoAtendimento, Date data);

    Collection<Atendimento> findByAtivoOrderByDataAscHorarioInicioAsc(Boolean ativo);

    Collection<Atendimento> findByDataAndFormaAtendimentoAndSituacaoAtendimentoNotIn(Date data, FormaAtendimentoEnum formaAtendimentoEnum, Collection<SituacaoAtendimento> situacaoAtendimentos);

    Collection<Atendimento> findByUsuarioCadastrouIdOrderByDataAscHorarioInicioAsc(Integer idUsuario);

    Collection<Atendimento> findByAtendenteIdOrderByDataAscHorarioInicioAsc(Integer idPessoa);

    Collection<Atendimento> findByBeneficiarioCpfOrderByDataAscHorarioInicioAsc(String beneficiarioCpf);

    Collection<Atendimento> findByUsuarioCadastrouIdAndBeneficiarioCpfOrderByDataAscHorarioInicioAsc(Integer idUsuario, String beneficiarioCpf);

    Collection<Atendimento> findByAtendenteIdAndBeneficiarioCpfOrderByDataAscHorarioInicioAsc(Integer idPessoa, String beneficiarioCpf);

    @Modifying
    @Query("UPDATE Atendimento a SET a.situacaoAtendimento = :situacaoAtendimento WHERE a.id = :id")
    void atualizarSituacao(@Param(value = "situacaoAtendimento") SituacaoAtendimento situacaoAtendimento, @Param(value = "id") Integer idAtendimento);

    Atendimento findBySenhaAndData(Integer senha, Date data);

    Collection<Atendimento> findByAtendenteIdAndDataAndHorarioInicioIdBetween(Integer pessoaId, Date data, Integer horarioInicio, Integer horarioFim);

    Collection<Atendimento> findByAtendenteIdAndDataBetween(Integer pessoaId, Date dataInicio, Date dataFim);

    Collection<Atendimento> findByBeneficiarioIdAndBeneficiarioCpfOrderByDataAscHorarioInicioAsc(Integer beneficiarioId, String beneficiarioCpf);

    Collection<Atendimento> findByBeneficiarioIdOrderByDataAscHorarioInicioAsc(Integer pessoaId);

    Collection<Atendimento> findByBeneficiarioIdAndBeneficiarioCpf(Integer beneficiarioId, String beneficiarioCpf);

    Collection<Atendimento> findByBeneficiarioId(Integer beneficiarioId);

    Collection<Atendimento> findByAtendenteIdAndDataAndSituacaoAtendimentoIdIn(Integer usuarioId, Date data, Collection<Integer> situacaoAtendimentoId);

    Collection<Atendimento> findByAtendenteIdAndDataGreaterThanEqual(Integer pessoaId, Date data) throws ServiceException;
}
