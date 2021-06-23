package bank.validators.clientValidators;

import bank.interfaces.regExp.ClientRegExp;
import com.vaadin.data.validator.RegexpValidator;

public class PhoneValidator extends RegexpValidator
{
    private static final String PATTERN = ClientRegExp.PHONE_REG_EXP;

    public PhoneValidator(String errorMessage)
    {
        super(errorMessage, PATTERN, true);
    }
}