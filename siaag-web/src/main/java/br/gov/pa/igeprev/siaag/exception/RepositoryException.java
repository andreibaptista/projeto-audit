/*
 *  DAOException
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
 * persistência.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 23/11/2016
 *
 */
public class RepositoryException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RepositoryException() {
		super();
	}
	
	public RepositoryException(Throwable throwable) {
		super(throwable);
	}
	
	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
