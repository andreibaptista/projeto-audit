/*
 *  ManagerBeanDefault
 *
 *  1.0.0
 *
 *  © Copyright 2018, Instituto de Gestão Previdenciária do Estado do Pará
 *  http://www.igeprev.pa.gov.br/
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;

import br.gov.pa.igeprev.siaag.enumeration.OperacaoEnum;
import br.gov.pa.igeprev.siaag.exception.RepositoryException;
import br.gov.pa.igeprev.siaag.exception.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.UnexpectedRollbackException;

/**
 * Classe abstrata que contém metodos, mensagens e controles padrões para ser utilizados em um CRUD.
 *
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 10/03/2017
 */
public abstract class ManagerBeanDefault extends Bean implements Serializable {

    private static final long serialVersionUID = 8626205926985603355L;

    protected static final Logger LOGGER = Logger.getLogger(ManagerBeanDefault.class);

    private OperacaoEnum operacao = OperacaoEnum.LISTANDO;

    private boolean erroValidacao = false;
    private boolean redirecionaTela = false;

    public abstract void inicializacao() throws RepositoryException, ServiceException;

    public abstract String getNomeFormulario();

    //TODO: Melhorar as mensagem de trace
    protected abstract String getMsgTraceAlteracao();

    protected abstract String getMsgTraceInclusao();

    protected abstract String getMsgTraceExclusao();

    protected abstract void novoRegistro() throws RepositoryException, ServiceException;

    protected abstract void editarRegistro(Object obj) throws RepositoryException, ServiceException;

    protected abstract void visualizarRegistro(Object obj) throws RepositoryException, ServiceException;

    protected abstract void cancelarOperacao() throws RepositoryException, ServiceException;

    protected abstract void buscarListaEntidades() throws RepositoryException, ServiceException;

    protected abstract void remover() throws RepositoryException, ServiceException;

    protected abstract void salvar() throws RepositoryException, ServiceException;

    @Inject
    protected SegurancaBean segurancaBean;

    @PostConstruct
    public void posContructor() throws Exception {

        String traceInicial;
        traceInicial = "Acessou o formulário " + getNomeFormulario();
        LOGGER.info(traceInicial);

        try {
            inicializacao();
            if (segurancaBean.getObjeto() != null && segurancaBean.getCadastro()) {
                editar(segurancaBean.getObjeto());
                setOperacao(OperacaoEnum.EDITANDO);
            } else if (segurancaBean.getObjeto() == null && segurancaBean.getCadastro()) {
                novo();
                setOperacao(OperacaoEnum.CADASTRANDO);
            } else {
                setOperacao(OperacaoEnum.LISTANDO);
            }
            setRedirecionaTela(false);
        } catch (ServiceException | RepositoryException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
            showErrorMessage(getMessage("msgErroCarregarRegistros"));
            throw new Exception(e);
        } catch (Exception e) {
            showErrorMessage();
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            String traceFinal;
            traceFinal = "Saiu do formulário " + getNomeFormulario();
            LOGGER.info(traceFinal);
            segurancaBean.setObjeto(null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void gravar() {
        try {
            String trace;
            if (isCadastrando()) {
                if (getMsgTraceInclusao() == null || getMsgTraceInclusao().length() == 0) {
                    trace = "Cadastro objeto no formulário " + getNomeFormulario();
                } else {
                    trace = getMsgTraceInclusao();
                }
            } else {
                if (getMsgTraceAlteracao() == null || getMsgTraceAlteracao().length() == 0) {
                    trace = "Alterou objeto no formulário " + getNomeFormulario();
                } else {
                    trace = getMsgTraceAlteracao();
                }
            }

            salvar();

            if (!erroValidacao && !redirecionaTela) {
                if (isCadastrando()) {
                    showSuccessMessage();
                } else if (isEditando()) {
                    showSuccessMessage();
                }
                setOperacaoListando();
                buscarListaEntidades();

                LOGGER.info(trace);
            }

            erroValidacao = false;
        } catch (RepositoryException e) {
            if (isCadastrando()) showErrorMessage(getMessage("ErroCadastrar"));
            else showErrorMessage(getMessage("ErroAlterar"));
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (ServiceException e) {
            if(e.getMessage().equals("mensagemERROCpfExistente")) showErrorMessage(getMessage("mensagemErroGenericaMotivo",getMessage(e.getMessage())));
            else showErrorMessage(getMessage(e.getMessage()));
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showErrorMessage();
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void excluir() {
        try {
            String trace = "";
            if (getMsgTraceExclusao() == null || getMsgTraceExclusao().length() == 0) {
                trace = "Excluiu objeto no formulário " + getNomeFormulario();
            } else {
                trace = getMsgTraceExclusao();
            }

            remover();
            showSuccessMessage();
            setOperacaoListando();
            LOGGER.info(trace);
            buscarListaEntidades();

        } catch (RepositoryException e) {
            showErrorMessage(getMessage("ErroExcluir"));
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (ServiceException e) {
            showErrorMessage(getMessage(e.getMessage()));
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (RollbackException | UnexpectedRollbackException | DataIntegrityViolationException | ConstraintViolationException | org.hibernate.exception.ConstraintViolationException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
            showErrorMessage(getMessage("mensagemErroExclusaoObjetosDependentes"));
        } catch (Exception e) {
            showErrorMessage();
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void novo() throws RepositoryException, ServiceException {
        setOperacaoCadastrando();
        novoRegistro();
        setRedirecionaTela(false);
    }

    public void editar(Object obj) throws RepositoryException, ServiceException {
        this.setOperacaoEditando();
        editarRegistro(obj);
        setRedirecionaTela(false);
    }

    public void visualizar(Object obj) throws RepositoryException, ServiceException {
        this.setOperacaoVisualizando();
        visualizarRegistro(obj);
        setRedirecionaTela(false);
		/*if(pagina != null && !pagina.isEmpty()){
			redirect(pagina);
		}*/
    }

    public void cancelar() throws RepositoryException, ServiceException {
        setOperacaoListando();
        cancelarOperacao();
        setRedirecionaTela(false);
    }

    public void setOperacaoListando() {
        this.operacao = OperacaoEnum.LISTANDO;
    }

    public void setOperacaoVisualizando() {
        this.operacao = OperacaoEnum.VISUALIZANDO;
    }

    public void setOperacaoCadastrando() {
        this.operacao = OperacaoEnum.CADASTRANDO;
    }

    public void setOperacaoEditando() {
        this.operacao = OperacaoEnum.EDITANDO;
    }

    public void setOperacao(OperacaoEnum operacao) {
        this.operacao = operacao;
    }

    public void setOperacao(String operacao) {
        if(operacao.equals("LISTANDO")){
            this.operacao = OperacaoEnum.LISTANDO;
        }else if(operacao.equals("CADASTRANDO")){
            this.operacao = OperacaoEnum.CADASTRANDO;
        }
    }

    public boolean isListando() {
        return operacao == OperacaoEnum.LISTANDO;
    }

    public boolean isVisualizando() {
        return operacao == OperacaoEnum.VISUALIZANDO;
    }

    public boolean isCadastrando() {
        return this.operacao == OperacaoEnum.CADASTRANDO;
    }

    public boolean isEditando() {
        return this.operacao == OperacaoEnum.EDITANDO;
    }

    public boolean isErroValidacao() {
        return erroValidacao;
    }

    public void setErroValidacao(boolean erroValidacao) {
        this.erroValidacao = erroValidacao;
    }

    public boolean isRedirecionaTela() {
        return redirecionaTela;
    }

    public void setRedirecionaTela(boolean redirecionaTela) {
        this.redirecionaTela = redirecionaTela;
    }
}