package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.Atendimento;
import br.gov.pa.igeprev.siaag.model.SituacaoAtendimento;

import java.util.Collection;
import java.util.Date;

public interface IAtendimentoService {

    Atendimento findById(int id) throws ServiceException;

    Collection<Atendimento> findAll() throws ServiceException;

    Atendimento salvar(Atendimento atendimento) throws ServiceException;

    Atendimento editar(Atendimento atendimento) throws ServiceException;

    Integer excluir(int id) throws ServiceException;

    Collection<Atendimento> findByDataAndFormaAtendimentoAndSituacaoAtendimentoNotIn(Date data, FormaAtendimentoEnum formaAtendimentoEnum, Collection<SituacaoAtendimento> situacaoAtendimentos) throws ServiceException;

    Collection<Atendimento> findByUsuarioCadastrouId(Integer idUsuario) throws ServiceException;

    Collection<Atendimento> findByAtendenteId(Integer idUsuario) throws ServiceException;

    Collection<Atendimento> findByUsuarioCadastrouIdAndBeneficiarioCpf(Integer idUsuario, String beneficiarioCpf) throws ServiceException;

    Collection<Atendimento> findByAtendenteIdAAndBeneficiarioCpf(Integer idUsuario, String beneficiarioCpf) throws ServiceException;

    Collection<Atendimento> findByBeneficiarioCpf(String beneficiarioCpf) throws ServiceException;

    void atualizarSituacao(SituacaoAtendimento situacaoAtendimento, Integer idAtendimento) throws ServiceException;

    Collection<Atendimento> findAllByAtivoOrderByDataHorarioInicio(Boolean ativo) throws ServiceException;

    Integer findSenhaAtualByData(Date data) throws ServiceException;

    /**
     * Busca atendimento existente para validação na hora de salvar o atendimento.
     *
     * @param ativo             se o atendimento esta ativo ou não
     * @param idBeneficiario    id do beneficiário
     * @param idTipoAtendimento id do tipo de atendimento
     * @param data              data do atendimento
     * @return Atendimento
     */
    Atendimento findByAtivoAndBeneficiarioIdAndTipoAtendimentoIdAndData(Boolean ativo, Integer idBeneficiario, Integer idTipoAtendimento, Date data) throws ServiceException;

    Atendimento findBySenhaAndData(Integer senha, Date data) throws ServiceException;

    Collection<Atendimento> findByAtendenteIdAndDataAndHorarioInicioIdBetween(Integer atendente, Date data, Integer horarioInicio, Integer horarioFim) throws ServiceException;

    Collection<Atendimento> findByAtendenteIdAndDataBetween(Integer atendenteId, Date dataInicio, Date dataFim) throws ServiceException;

    Collection<Atendimento> findByAtendenteIdAndDataAndSituacaoAtendimentoNotAgendado(Integer usuarioId, Date data) throws ServiceException;

    Collection<Atendimento> findByAtendenteIdAndDataGreaterThanEqual(Integer pessoaId, Date data) throws ServiceException;
}
