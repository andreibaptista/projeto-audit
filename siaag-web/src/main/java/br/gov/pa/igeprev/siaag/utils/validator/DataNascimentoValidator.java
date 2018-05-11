/*
 *  EmailValidator
 *
 *  1.0.0
 *
 *  © Copyright 2018, Instituto de Gestão Previdenciária do Estado do Pará
 *  http://www.igeprev.pa.gov.br/
 *  Todos os direitos reservados e protegidos pela Lei nº9.610/98.
 */
package br.gov.pa.igeprev.siaag.utils.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSF Validator para validar Nomes.
 *
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 01/03/2018
 */
@FacesValidator("br.gov.pa.igeprev.siaag.utils.validator.DataNascimentoValidator")
public class DataNascimentoValidator implements Validator {;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value != null && !value.toString().isEmpty()) {
            String data = value.toString();
            Date dataAtual = new Date(data);
            if (dataAtual.compareTo(new Date()) > 0) {
                FacesMessage msg = new FacesMessage("Validação da data de nascimento falhou.", "Valor inválido para o campo data de nascimento. Data não pode ser maior que a data atual.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }
}



