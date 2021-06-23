package bank.validators.clientValidators;

import bank.interfaces.regExp.ClientRegExp;
import com.vaadin.data.validator.RegexpValidator;

public class PassportNumberValidator extends RegexpValidator
{
    private static final String PATTERN = ClientRegExp.PASSPORT_NUMBER_REG_EXP;

    public PassportNumberValidator(String errorMessage)
    {
        super(errorMessage, PATTERN, true);
    }
}