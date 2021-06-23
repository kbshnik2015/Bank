package bank.interfaces.regExp;

public interface ClientRegExp
{
    String NAME_REG_EXP ="^[a-zA-Zа-яА-Я]+$";
    String PATRONYMIC_REG_EXP ="^[a-zA-Zа-яА-Я]+$|()";
    String EMAIL_REG_EXP ="^([a-zA-Z0-9_\\.\\-+])+@[a-zA-Z0-9-.]+\\.[a-zA-Z0-9-]{2,}$";
    String PASSPORT_NUMBER_REG_EXP ="^\\d{3} - \\d{3}|\\d{3}-\\d{3}$";
    String PHONE_REG_EXP ="(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?";
}
