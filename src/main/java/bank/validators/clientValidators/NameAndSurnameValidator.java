package bank.validators.clientValidators;

import bank.interfaces.regExp.ClientRegExp;
import com.vaadin.data.validator.RegexpValidator;

public class NameAndSurnameValidator extends RegexpValidator
{
    private static final String PATTERN = ClientRegExp.NAME_REG_EXP;

    public NameAndSurnameValidator(String errorMessage)
    {
        super(errorMessage, PATTERN, true);
    }
}