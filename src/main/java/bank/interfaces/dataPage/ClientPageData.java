package bank.interfaces.dataPage;

public interface ClientPageData
{
    String NAME = "Name";
    String SURNAME = "Surname";
    String PATRONYMIC = "Patronymic";
    String EMAIL = "Email";
    String PHONE = "Phone number";
    String PASSPORT_NUMBER = "Passport number";
    String BUTTONS_PANEL_CREATE_BUTTON = "Create client";
    String BUTTONS_PANEL_EDIT_BUTTON = "Edit client";
    String BUTTONS_PANEL_DELETE_BUTTON = "Delete client(s)";
    String DELETE_WINDOW_QUESTION = "Are you sure you want to delete the selected client(s)?";
    String DELETE_WINDOW_POSITIVE_ANSWER = "yes";
    String DELETE_WINDOW_NEGATIVE_ANSWER = "cancel";
    String NAME_ERROR_VALIDATION = "The name must consist of Russian (а-я) or latin (a-z) letters!";
    String SURNAME_ERROR_VALIDATION = "The surname must consist of Russian (а-я) or latin (a-z) letters!";
    String PATRONYMIC_ERROR_VALIDATION =
            "The patronymic must consist of Russian (а-я) or latin (a-z) letters or remain empty!";
    String EMAIL_ERROR_VALIDATION = "Email must match the template 'username@domainname'";
    String PHONE_ERROR_VALIDATION =
            " The phone number must match one of the following patterns:\n (***) *** - ** - **\n or * (***) ** - *** - **\n or ************\n or  ************\n or where * is an integer.";
    String PASSPORT_NUMBER_ERROR_VALIDATION =
            "The passport number must match one of the following patterns: ***-*** ";
    String SAVE_BUTTON = "save";
    String ERROR_NAME_NOTIFICATION = "The entered name is incorrect";
    String ERROR_SURNAME_NOTIFICATION = "The entered surname  is incorrect";
    String ERROR_PATRONYMIC_NOTIFICATION = "The entered patronymic  is incorrect";
    String ERROR_EMAIL_NOTIFICATION = "The entered email is incorrect";
    String ERROR_PHONE_NOTIFICATION = "The entered phone number is incorrect";
    String ERROR_PASSPORT_NUMBER_NOTIFICATION = "The entered passport number  is incorrect";
    String CANCEL = "cancel";
    String BANKS = "Available banks";
}
