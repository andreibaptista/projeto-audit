/*
 *  BusinessException
 *  
 *  1.0.0
 *  
 *  © Copyright 2017, Tribunal de Justiça do Estado Roraima
 *  http://www.tjrr.jus.br
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.exception;

/**
 * Classe responsável por lançar exceção quando um erro ocorre na camada de
 * negócio.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 23/11/2016
 *
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException(String m) {
		super(m);
	}

	public ServiceException(String m, Throwable t) {
		super(m, t);
	}
}
