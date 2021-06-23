package bank.interfaces.dataPage;

public interface LoanProcessingPageData
{

    String CREATE_CLIENT = "Create new client";
    String SELECT_CLIENT = "Select exist client";
    String PICK_LOAN = "Pick up a loan";
    String CLIENT_ERROR = "Client not specified!";
    String MONEY_ERROR = "The indicated incorrect value for the loan amount!";
    String YEARS_ERROR = "The specified value for the number of years is incorrect!";
    String BANK_ERROR = "The client is not registered with any bank!";
    String CREDIT_ERROR = "No suitable loan offer was found, try to reduce the amount of the loan!";
    String CHOSE_CREDIT = "Choose a suitable offer";
    String CALCULATE = "Calculate payment schedule";
    String SAVE_CREDIT = "Accept and save";
    String COMPLITE = "Loan successfully completed!";
}
