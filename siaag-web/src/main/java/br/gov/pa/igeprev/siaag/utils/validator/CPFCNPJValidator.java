/*
 *  CPFCNPJValidator
 *  
 *  1.0.0
 *  
 *  © Copyright 2018, Instituto de Gestão Previdenciária do Estado do Pará
 *  http://www.igeprev.pa.gov.br/
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.utils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

/**
 * JSF Validator para CPF ou CNPJ.
 * 
 * @author Aleixo Porpino
 * @version 1.0.0
 * @since 31/03/2017
 */
@FacesValidator("CPFCNPJValidator")
public class CPFCNPJValidator implements Validator {

	/**
	 * Realiza a validação tanto de CPF quanto de CNPJ no componente JSF.
	 * 
	 */
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object valor) throws ValidatorException {
		try {
			CPFValidator cpfValidator = new CPFValidator();
			CNPJValidator cnpjValidator = new CNPJValidator();
			
			if (valor != null) {
				String cpfCnpj = valor.toString();
				
				if(validarDigitos(cpfCnpj)) throw new Exception("CPF Formado apenas pelo digito 0 (Zero)");
				
				if(cpfCnpj.length() > 11) {
					cnpjValidator.assertValid(cpfCnpj);
				} else {
					cpfValidator.assertValid(cpfCnpj);
				}
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Digite um CPF válido.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
	
	private boolean validarDigitos(String valor) {
		 Pattern p = Pattern.compile("^[0]+$");
		 Matcher m = p.matcher(valor);
		 return m.matches();
	}
}
