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
 * JSF Validator para validar Nomes.
 *
 * @author Aleixo Porpino Filho
 * @version 1.0.0
 * @since 28/02/2017
 */
@FacesValidator("br.gov.pa.igeprev.siaag.utils.validator.NomeValidator")
public class NomeValidator implements Validator {
    //      Números {0-9}
    //		Ponto de exclamação {!}
    //		Abrir parêntese {(}
    //		Fechar parêntese {)}
    //		Travessão {-}
    //		Ponto {.}
    //		Ponto de interrogação {?}
    //		Abrir colchete {[}
    //		Fechar colchete {]}
    //		Sublinhado {_}
    private static final String NOME_PATTERN = "[a-zÀ-ÿA-ZÀ-Ÿ\\s]+";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        pattern = Pattern.compile(NOME_PATTERN);
        matcher = pattern.matcher(value.toString());
        if (!matcher.matches()) {
            FacesMessage msg = new FacesMessage("Validação do nome falhou.", "Nome não pode conter números ou caracteres especiais.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}



