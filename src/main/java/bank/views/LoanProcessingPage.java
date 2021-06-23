package bank.views;

import bank.entity.*;
import bank.interfaces.dataPage.ClientPageData;
import bank.interfaces.dataPage.LoanProcessingPageData;
import bank.interfaces.regExp.ClientRegExp;
import bank.services.BankService;
import bank.services.ClientService;
import bank.services.CreditOfferService;
import bank.services.OneTimePaymentService;
import bank.validators.clientValidators.NameAndSurnameValidator;
import bank.validators.clientValidators.PassportNumberValidator;
import bank.validators.clientValidators.PatronymicValidator;
import bank.validators.clientValidators.PhoneValidator;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringView (name = "")
public class LoanProcessingPage extends VerticalLayout implements View
{

    @Autowired
    ClientService clientService;

    @Autowired
    CreditOfferService creditOfferService;

    @Autowired
    OneTimePaymentService oneTimePaymentService;

    @Autowired
    BankService bankService;

    NativeSelect<Client> clientSelect;
    TextField numberOfYears = new TextField("Number of years");
    TextField amountOfMoney = new TextField("Amount of money");

    public static boolean isNumeric(String str)
    {
        return str.matches("\\d+(\\.\\d+)?");
    }

    public static boolean isValidYear(String year)
    {
        return (year.matches("^\\d{2}|\\d") && Integer.parseInt(year) <= 30 && Integer.parseInt(year) > 0);
    }

    @PostConstruct
    void init()
    {
        Button createNewClient = new Button(
                LoanProcessingPageData.CREATE_CLIENT,
                clickEvent -> getUI().addWindow(createWindowForCreatingClient())
        );
        createNewClient.setStyleName(ValoTheme.BUTTON_PRIMARY);
        clientSelect = new NativeSelect<>(LoanProcessingPageData.SELECT_CLIENT, clientService.getAll());
        clientSelect.setRequiredIndicatorVisible(true);
        amountOfMoney.setRequiredIndicatorVisible(true);
        numberOfYears.setRequiredIndicatorVisible(true);
        HorizontalLayout yearsAndSum = new HorizontalLayout();
        yearsAndSum.addComponents(amountOfMoney, numberOfYears);
        Button pickUpLoanButton = new Button(LoanProcessingPageData.PICK_LOAN, clickEvent -> pickUpLoan());
        pickUpLoanButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        addComponents(clientSelect, createNewClient, yearsAndSum, pickUpLoanButton);
    }

    public void pickUpLoan()
    {
        if (clientSelect.getValue() == null)
        {
            Notification error = new Notification(LoanProcessingPageData.CLIENT_ERROR, Notification.Type.ERROR_MESSAGE);
            error.setDelayMsec(1500);
            error.show(getUI().getPage());
        }
        else if (!isNumeric(amountOfMoney.getValue()) && amountOfMoney.getValue() == null &&
                Double.parseDouble(amountOfMoney.getValue()) <= 0)
        {
            Notification error = new Notification(LoanProcessingPageData.MONEY_ERROR, Notification.Type.ERROR_MESSAGE);

            error.setDelayMsec(1500);
            error.show(getUI().getPage());
        }
        else if (!isValidYear(numberOfYears.getValue()))
        {
            Notification error = new Notification(LoanProcessingPageData.YEARS_ERROR, Notification.Type.ERROR_MESSAGE);
            error.setDelayMsec(1500);
            error.show(getUI().getPage());
        }
        else if (clientSelect.getValue()
                .getBanks()
                .isEmpty())
        {
            Notification error = new Notification(LoanProcessingPageData.BANK_ERROR, Notification.Type.ERROR_MESSAGE);
            error.setDelayMsec(1500);
            error.show(getUI().getPage());
        }
        else if (getSuitableCredits().isEmpty())
        {
            Notification error = new Notification(
                    LoanProcessingPageData.CREDIT_ERROR,
                    Notification.Type.ERROR_MESSAGE);
            error.setDelayMsec(1500);
            error.show(getUI().getPage());
        }
        else
        {
            getUI().addWindow(createWindowForPickUpLoan());
        }
    }

    private Window createWindowForPickUpLoan()
    {
        Window window = new Window();
        VerticalLayout main = new VerticalLayout();
        NativeSelect<Credit> creditSelect = new NativeSelect<>(LoanProcessingPageData.CHOSE_CREDIT, getSuitableCredits());
        creditSelect.setRequiredIndicatorVisible(true);
        Button confirmButton = new Button(LoanProcessingPageData.CALCULATE, clickEvent -> {
            getUI().removeWindow(window);
            getUI().addWindow(createWindowForAcceptCredit(creditSelect.getValue()));
        });
        confirmButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        main.addComponents(creditSelect, confirmButton);
        window.setCaption("Select credit");
        window.setContent(main);
        window.setModal(true);
        window.center();
        return window;
    }

    private Window createWindowForAcceptCredit(Credit credit)
    {
        Window window = new Window();
        VerticalLayout main = new VerticalLayout();
        List<OneTimePayment> paymentSchedule = calculatePaymentSchedule(credit);
        Grid<OneTimePayment> paymentScheduleGrid = new Grid<>(OneTimePayment.class);
        paymentScheduleGrid.setItems(paymentSchedule);
        CreditHistoryPage.paymentScheduleConfig(paymentScheduleGrid);
        Button acceptButton = new Button(LoanProcessingPageData.SAVE_CREDIT, clickEvent -> {
            acceptAndSave(credit, paymentSchedule);
            getUI().removeWindow(window);
            new Notification(LoanProcessingPageData.COMPLITE,
                    Notification.Type.HUMANIZED_MESSAGE).show(getUI().getPage());
        });
        acceptButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        main.addComponents(paymentScheduleGrid, acceptButton);
        window.setCaption("Credit offer");
        window.setSizeFull();
        window.setContent(main);
        window.setModal(true);
        window.center();
        return window;

    }

    public void acceptAndSave(Credit credit, List<OneTimePayment> paymentSchedule)
    {

        CreditOffer creditOffer = new CreditOffer();
        creditOffer.setPaymentSchedule(paymentSchedule);
        creditOffer.setCredit(credit);
        creditOffer.setCreditAmount(Double.parseDouble(amountOfMoney.getValue()));
        creditOffer.setClient(clientSelect.getSelectedItem()
                .get());
        creditOfferService.save(creditOffer);
        for (OneTimePayment oneTimePayment : paymentSchedule)
        {
            oneTimePayment.setCreditOffer(creditOffer);
            oneTimePaymentService.save(oneTimePayment);
        }
    }

    private List<OneTimePayment> calculatePaymentSchedule(Credit credit)
    {
        List<OneTimePayment> paymentSchedule = new ArrayList<>();
        int numberOfMonths = Integer.parseInt(numberOfYears.getValue()) * 12;
        double loanBodyAmount = rounding(Double.parseDouble(amountOfMoney.getValue()) / numberOfMonths);
        double loanBalance = Double.parseDouble(amountOfMoney.getValue());
        for (int i = 0; i < numberOfMonths; i++)
        {
            long MONTH_IN_MILLI_SEC = 2592000000L;
            Date payDay = new Date(System.currentTimeMillis() + MONTH_IN_MILLI_SEC * (i + 1));
            double interestRepaymentAmount = rounding(loanBalance * credit.getPercent() / 100 / 12) ;
            double amountOfPayment = rounding(interestRepaymentAmount + loanBodyAmount);
            OneTimePayment oneTimePayment =
                    new OneTimePayment(payDay, amountOfPayment, loanBodyAmount, interestRepaymentAmount);
            paymentSchedule.add(oneTimePayment);
            loanBalance = rounding(loanBalance - loanBodyAmount);
        }
        return paymentSchedule;
    }

    private Window createWindowForCreatingClient()
    {
        Client client = new Client();
        Window window = new Window();
        VerticalLayout main = new VerticalLayout();
        TextField name;
        TextField surname;
        TextField patronymic;
        TextField phone;
        TextField email;
        TextField passportNumber;
        name = new TextField(ClientPageData.NAME);
        surname = new TextField(ClientPageData.SURNAME);
        patronymic = new TextField(ClientPageData.PATRONYMIC);
        phone = new TextField(ClientPageData.PHONE);
        email = new TextField(ClientPageData.EMAIL);
        passportNumber = new TextField(ClientPageData.PASSPORT_NUMBER);

        name.setRequiredIndicatorVisible(true);
        surname.setRequiredIndicatorVisible(true);
        phone.setRequiredIndicatorVisible(true);
        passportNumber.setRequiredIndicatorVisible(true);
        new Binder<Client>().forField(name)
                .withValidator(new NameAndSurnameValidator(ClientPageData.NAME_ERROR_VALIDATION))
                .bind(Client :: getName, Client :: setName);
        new Binder<Client>().forField(surname)
                .withValidator(new NameAndSurnameValidator(ClientPageData.SURNAME_ERROR_VALIDATION))
                .bind(Client :: getSurname, Client :: setSurname);
        new Binder<Client>().forField(patronymic)
                .withValidator(new PatronymicValidator(ClientPageData.PATRONYMIC_ERROR_VALIDATION))
                .bind(Client :: getPatronymic, Client :: setPatronymic);
        new Binder<Client>().forField(phone)
                .withValidator(new PhoneValidator(ClientPageData.PHONE_ERROR_VALIDATION))
                .bind(Client :: getPhone, Client :: setPhone);
        new Binder<Client>().forField(passportNumber)
                .withValidator(new PassportNumberValidator(ClientPageData.PASSPORT_NUMBER_ERROR_VALIDATION))
                .bind(Client :: getPassportNumber, Client :: setPassportNumber);
        new Binder<Client>().forField(email)
                .withValidator(new EmailValidator(ClientPageData.EMAIL_ERROR_VALIDATION))
                .bind(Client :: getEmail, Client :: setEmail);
        HorizontalLayout firstRow = new HorizontalLayout();
        HorizontalLayout secondRow = new HorizontalLayout();
        HorizontalLayout buttonsRow = new HorizontalLayout();
        CheckBoxGroup<Bank> bankCheckBox = new CheckBoxGroup<>("Chose banks", bankService.getAll());
        bankCheckBox.setItemCaptionGenerator(Bank :: getName);
        Button saveButton = new Button(ClientPageData.SAVE_BUTTON, clickEvent -> {
            if (!isValidName(name.getValue()))
            {
                new Notification(
                        ClientPageData.ERROR_NAME_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }
            else if (!isValidName(surname.getValue()))
            {
                new Notification(
                        ClientPageData.ERROR_SURNAME_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }
            else if (!isValidPatronymic(patronymic.getValue()))
            {
                new Notification(
                        ClientPageData.ERROR_PATRONYMIC_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }
            else if (!isValidEmail(email.getValue()))
            {
                new Notification(
                        ClientPageData.ERROR_EMAIL_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }
            else if (!isValidPhone(phone.getValue()))
            {
                new Notification(
                        ClientPageData.ERROR_PHONE_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }
            else if (!isValidPassportNumber(passportNumber.getValue()))
            {
                new Notification(
                        ClientPageData.ERROR_PASSPORT_NUMBER_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }
            else
            {
                client.setName(name.getValue());
                client.setSurname(surname.getValue());
                client.setPatronymic(patronymic.getValue());
                client.setPhone(phone.getValue());
                client.setEmail(email.getValue());
                client.setPassportNumber(passportNumber.getValue());
                client.setBanks(bankCheckBox.getSelectedItems());
                clientService.save(client);

                clientSelect.setItems(clientService.getAll());
                getUI().removeWindow(window);
            }
        });

        saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

        Button cancelButton = new Button(ClientPageData.CANCEL, clickEvent -> getUI().removeWindow(window));
        cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
        firstRow.addComponents(name, surname, patronymic);
        secondRow.addComponents(phone, email, passportNumber);
        buttonsRow.addComponents(saveButton, cancelButton);
        VerticalLayout bankLayout = new VerticalLayout();
        bankLayout.addComponents(bankCheckBox);
        bankLayout.setMargin(true);
        main.addComponents(firstRow, secondRow, bankCheckBox, buttonsRow);
        setCaption("Crating client");
        window.setContent(main);
        window.setModal(true);
        window.center();
        return window;
    }

    private boolean isValidName(String name)
    {
        return name.matches(ClientRegExp.NAME_REG_EXP);
    }

    private boolean isValidPatronymic(String patronymic)
    {
        return patronymic.matches(ClientRegExp.PATRONYMIC_REG_EXP);
    }

    private boolean isValidEmail(String email)
    {
        return email.matches(ClientRegExp.EMAIL_REG_EXP);
    }

    private boolean isValidPassportNumber(String passportNumber)
    {
        return passportNumber.matches(ClientRegExp.PASSPORT_NUMBER_REG_EXP);
    }

    private boolean isValidPhone(String phone)
    {
        return phone.matches(ClientRegExp.PHONE_REG_EXP);
    }

    public Set<Credit> getSuitableCredits()
    {
        Set<Bank> banks = clientSelect.getValue()
                .getBanks();
        Set<Credit> credits = new HashSet<>();
        for (Bank bank : banks)
        {
            credits.addAll(bank.getCredits());
        }
        credits = credits.stream()
                .filter(credit -> credit.getLimit() >= Double.parseDouble(amountOfMoney.getValue()))
                .collect(Collectors.toSet());
        return credits;
    }
    public double rounding(double d){
        BigDecimal bd = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {

    }

}
