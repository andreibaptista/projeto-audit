/*
 *  InputConverter
 *  
 *  1.0.0
 *  
 *  © Copyright 2018, Instituto de Gestão Previdenciária do Estado do Pará
 *  http://www.igeprev.pa.gov.br/
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.utils.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.pa.igeprev.siaag.utils.StringUtils;

/**
 * Limpa os caracteres especiais de um input.
 * 
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 24/02/2017
 */
@FacesConverter(value="br.gov.pa.igeprev.siaag.utils.converter.InputConverter")
public class InputConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		return StringUtils.removeSpecialCharacter(valor);
	}
	
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object valor) {
		return valor.toString();
	}
}