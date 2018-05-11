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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSF Validator para validar E-mails.
 *
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 08/02/2017
 */
@FacesValidator("br.gov.pa.igeprev.siaag.utils.validator.EmailValidator")
public class EmailValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value != null && !value.toString().isEmpty()) {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(value.toString());
            if (!matcher.matches()) {
                FacesMessage msg = new FacesMessage("Validação de e-mail falhou.", "Valor inválido para o campo e-mail. Formato de e-mail inválido.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }
}
