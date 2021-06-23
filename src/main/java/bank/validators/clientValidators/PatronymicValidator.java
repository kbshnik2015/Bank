package bank.validators.clientValidators;

import bank.interfaces.regExp.ClientRegExp;
import com.vaadin.data.validator.RegexpValidator;

public class PatronymicValidator extends RegexpValidator
{
    private static final String PATTERN = ClientRegExp.PATRONYMIC_REG_EXP;

    public PatronymicValidator(String errorMessage)
    {
        super(errorMessage, PATTERN, true);
    }
}
