/*
 *  CPFCNPJConverter
 *  
 *  1.0.0
 *  
 *  © Copyright 2018, Tribunal de Justiça do Estado Roraima
 *  http://www.igeprev.pa.gov.br/
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.utils.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.tinytype.CNPJ;
import br.com.caelum.stella.tinytype.CPF;
import br.gov.pa.igeprev.siaag.utils.StringUtils;


/**
 * Classe que coloca máscara de cpf ou cnpj em um componente JSF.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 24/02/2017
 */
@FacesConverter("cpfCNPJConverter")
public class CPFCNPJConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String str) {
		return StringUtils.removeSpecialCharacter(str);
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) {
		
		String numero = null;
		
		if(object != null && object instanceof String) {
			numero = (String) object;
			
			if(new CPF(numero).isValido()) {
				numero = new CPFFormatter().format(numero);
			} else if(new CNPJ(numero).isValid()) {
				numero = new CNPJFormatter().format(numero);
			}
		}
		
		return numero;
	}
}