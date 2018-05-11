package br.gov.pa.igeprev.siaag.service;

import br.gov.pa.igeprev.siaag.enumeration.FormaAtendimentoEnum;
import br.gov.pa.igeprev.siaag.enumeration.PerfilEnum;
import br.gov.pa.igeprev.siaag.enumeration.SituacaoAtendimentoEnum;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import br.gov.pa.igeprev.siaag.model.*;
import br.gov.pa.igeprev.siaag.repository.ArquivoRepository;
import br.gov.pa.igeprev.siaag.repository.AtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Classe de negócio da entidade Atendimento.
 *
 * @author José Aleixo Araujo Porpino Filho
 * @version 1.0
 * @since 19/01/2018
 */
@Service
public class AtendimentoService implements IAtendimentoService {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    PessoaService pessoaService;
    @Autowired
    ArquivoRepository arquivoRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public Collection<Date> datasDisponiveisPorTipoAgendamento(FormaAtendimentoEnum formaAtendimentoEnum) throws ServiceException {
        Query query = em.createNativeQuery("SELECT AG.DATA FROM SIAAG.TAB_AGENDA AG " +
                "            JOIN SIAAG.TAB_AGENDA_ITEM AGI ON AGI.ID_AGENDA = AG.ID " +
                "            WHERE AGI.ATIVO = 1 AND AG.ATIVO = 1 AND AG.DATA >= SYSDATE AND AGI.TIPO_AGENDAMENTO = UPPER(:tipoAgendamento) AND" +
                "                AGI.ID NOT IN (SELECT AT2.ID_AGENDA_ITEM FROM SIAAG.TAB_ATENDIMENTO AT2 WHERE AT2.ATIVO = 1 AND AT2.ID_SITUACAO_ATENDIMENTO <> 3 AND AT2.ID_SITUACAO_ATENDIMENTO <> 4) " +
                "GROUP BY AG.DATA " +
                "ORDER BY AG.DATA ");
        query.setParameter("tipoAgendamento", formaAtendimentoEnum.getValue());
        return query.getResultList();
    }

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Transactional(readOnly = true)
    public Atendimento findById(int id) throws ServiceException {
        return atendimentoRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findAll() throws ServiceException {
        return atendimentoRepository.findAll();
    }

    @Transactional
    public Atendimento salvar(Atendimento atendimento) throws ServiceException {
        //Realiza a verificação do usuário existente na base, caso não ele vai realizar o cadastro do usuário com a senha padrão.
        if (atendimento.getBeneficiario() != null) {
            Usuario usuarioExistente = usuarioService.findByLoginAndAtivo(atendimento.getBeneficiario().getCpf(), true);

            if ((usuarioExistente == null || usuarioExistente.getId() == null) || atendimento.getBeneficiario().getId() == null) {
                Usuario usuario = new Usuario();
                usuario.setLogin(atendimento.getBeneficiario().getCpf());
                usuario.setSenha("");
                usuario.setAtivo(true);
                usuario.setPrimeiroAcesso(true);
                usuario.setPessoa(atendimento.getBeneficiario());
                usuario.setPerfil(new Perfil(PerfilEnum.CLIENTE.getId()));
                usuario.setPontoAtendimentoSetor(null);
                usuario = usuarioService.salvar(usuario);
                atendimento.setBeneficiario(usuario.getPessoa());
            }else{
                pessoaService.salvar(atendimento.getBeneficiario());
            }
        }
        if (atendimento.getSenha() == null) atendimento.setSenha(findSenhaAtualByData(atendimento.getData()));
        atendimentoRepository.save(atendimento);
        List<Arquivo> arquivos = new ArrayList<>();
        if (atendimento.getArquivos() != null) {
            for (Arquivo arq : atendimento.getArquivos()) {
                Arquivo arquivo = new Arquivo();
                arquivo.setArquivo(arq.getArquivo());
                arquivo.setDescricao(arq.getDescricao());
                arquivo.setAtendimento(atendimento);
                arquivos.add(arquivo);
            }
            atendimento.setArquivos(null);
            if (!arquivos.isEmpty()) arquivoRepository.save(arquivos);
        }
        return atendimento;
    }

    @Transactional
    public Atendimento editar(Atendimento atendimento) throws ServiceException {
        Collection<Atendimento> Atendimentos = findAll();
        for (Atendimento p : Atendimentos) {
            if (p.getId().equals(atendimento.getId())) {
                return atendimentoRepository.save(atendimento);
            }
        }
        return null;
    }

    @Transactional
    public Integer excluir(int id) throws ServiceException {
        Collection<Atendimento> atendimentos = findAll();
        for (Atendimento p : atendimentos) {
            if (p.getId().equals(id)) {
                atendimentoRepository.delete(id);
                return id;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByDataAndFormaAtendimentoAndSituacaoAtendimentoNotIn(Date data, FormaAtendimentoEnum formaAtendimentoEnum, Collection<SituacaoAtendimento> situacaoAtendimentos) throws ServiceException {
        return atendimentoRepository.findByDataAndFormaAtendimentoAndSituacaoAtendimentoNotIn(data, formaAtendimentoEnum, situacaoAtendimentos);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByUsuarioCadastrouId(Integer idUsuario) throws ServiceException {
        return atendimentoRepository.findByUsuarioCadastrouIdOrderByDataAscHorarioInicioAsc(idUsuario);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByAtendenteId(Integer idPessoa) throws ServiceException {
        return atendimentoRepository.findByAtendenteIdOrderByDataAscHorarioInicioAsc(idPessoa);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByUsuarioCadastrouIdAndBeneficiarioCpf(Integer idUsuario, String beneficiarioCpf) throws ServiceException {
        return atendimentoRepository.findByUsuarioCadastrouIdAndBeneficiarioCpfOrderByDataAscHorarioInicioAsc(idUsuario, beneficiarioCpf);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByAtendenteIdAAndBeneficiarioCpf(Integer idPessoa, String beneficiarioCpf) throws ServiceException {
        return atendimentoRepository.findByAtendenteIdAndBeneficiarioCpfOrderByDataAscHorarioInicioAsc(idPessoa, beneficiarioCpf);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByBeneficiarioCpf(String beneficiarioCpf) throws ServiceException {
        return atendimentoRepository.findByBeneficiarioCpfOrderByDataAscHorarioInicioAsc(beneficiarioCpf);
    }

    @Transactional
    public void atualizarSituacao(SituacaoAtendimento situacaoAtendimento, Integer idAtendimento) throws ServiceException {
        atendimentoRepository.atualizarSituacao(situacaoAtendimento, idAtendimento);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findAllByAtivoOrderByDataHorarioInicio(Boolean ativo) throws ServiceException {
        return atendimentoRepository.findByAtivoOrderByDataAscHorarioInicioAsc(ativo);
    }

    @Transactional(readOnly = true)
    public Integer findSenhaAtualByData(Date data) throws ServiceException {
        try {
            Query query = em.createNativeQuery("SELECT max(senha) " +
                    "FROM SIAAG.TAB_ATENDIMENTO a " +
                    "WHERE DATA BETWEEN :data AND :data and FORMA_ATENDIMENTO = 'AGENDADO'");
            query.setParameter("data", data);
            BigDecimal senha = (BigDecimal) query.getSingleResult();
            if (senha == null) {
                senha = new BigDecimal("1000");
            } else {
                senha = senha.add(BigDecimal.ONE);
            }
            return Integer.parseInt(senha + "");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Atendimento findByAtivoAndBeneficiarioIdAndTipoAtendimentoIdAndData(Boolean ativo, Integer idBeneficiario, Integer idTipoAtendimento, Date data) throws ServiceException {
        return atendimentoRepository.findByAtivoAndBeneficiarioIdAndTipoAtendimentoIdAndData(ativo, idBeneficiario, idTipoAtendimento, data);
    }

    @Transactional(readOnly = true)
    public Atendimento findBySenhaAndData(Integer senha, Date data) throws ServiceException {
        return atendimentoRepository.findBySenhaAndData(senha, data);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByAtendenteIdAndDataAndHorarioInicioIdBetween(Integer idPessoa, Date data, Integer horarioInicioId, Integer horarioFimId) throws ServiceException {
        return atendimentoRepository.findByAtendenteIdAndDataAndHorarioInicioIdBetween(idPessoa, data, horarioInicioId, horarioFimId);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByAtendenteIdAndDataBetween(Integer atendenteId, Date dataInicio, Date dataFim) throws ServiceException {
        return atendimentoRepository.findByAtendenteIdAndDataBetween(atendenteId, dataInicio, dataFim);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByBeneficiarioIdAndBeneficiarioCpf(Integer beneficiarioId, String beneficiarioCpf) throws ServiceException {
        return atendimentoRepository.findByBeneficiarioIdAndBeneficiarioCpfOrderByDataAscHorarioInicioAsc(beneficiarioId, beneficiarioCpf);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByBeneficiarioId(Integer beneficiarioId) throws ServiceException {
        return atendimentoRepository.findByBeneficiarioIdOrderByDataAscHorarioInicioAsc(beneficiarioId);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByAtendenteIdAndDataAndSituacaoAtendimentoNotAgendado(Integer usuarioId, Date data) throws ServiceException {
        Collection<Integer> situacoes = new ArrayList<>();
        situacoes.add(SituacaoAtendimentoEnum.ATIVO.getId());
        situacoes.add(SituacaoAtendimentoEnum.REAGENDADO.getId());
        situacoes.add(SituacaoAtendimentoEnum.ATENDIDO.getId());
        situacoes.add(SituacaoAtendimentoEnum.PRE_ATENDIMENTO.getId());
        situacoes.add(SituacaoAtendimentoEnum.PRE_ATENDIDO.getId());
        return atendimentoRepository.findByAtendenteIdAndDataAndSituacaoAtendimentoIdIn(usuarioId, data, situacoes);
    }

    @Transactional(readOnly = true)
    public Collection<Atendimento> findByAtendenteIdAndDataGreaterThanEqual(Integer pessoaId, Date data) throws ServiceException {
        return atendimentoRepository.findByAtendenteIdAndDataGreaterThanEqual(pessoaId, data);
    }
}