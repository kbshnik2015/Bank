package bank.views;


import bank.entity.Bank;
import bank.entity.Client;
import bank.interfaces.dataPage.ClientPageData;
import bank.interfaces.regExp.ClientRegExp;
import bank.services.BankService;
import bank.services.ClientService;
import bank.validators.clientValidators.NameAndSurnameValidator;
import bank.validators.clientValidators.PassportNumberValidator;
import bank.validators.clientValidators.PatronymicValidator;
import bank.validators.clientValidators.PhoneValidator;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringView (name = "Clients")
public class ClientPage extends VerticalLayout implements View
{

    @Autowired
    private ClientService clientService;

    @Autowired
    private BankService bankService;

    private Grid<Client> grid;
    private Button editClientButton;
    private Button deleteClientsButton;
    private List<Client> selected;

    @PostConstruct
    void init()
    {
        configureButtons();
        configureGrid();

    }

    private void configureGrid()
    {
        grid = new Grid<>(Client.class);
        grid.setItems(clientService.getAll());
        grid.removeAllColumns();
        grid.setWidth("100%");
        grid.addColumn(Client :: getName)
                .setCaption(ClientPageData.NAME);
        grid.addColumn(Client :: getSurname)
                .setCaption(ClientPageData.SURNAME);
        grid.addColumn(Client :: getPatronymic)
                .setCaption(ClientPageData.PATRONYMIC);
        grid.addColumn(Client :: getPhone)
                .setCaption(ClientPageData.PHONE);
        grid.addColumn(Client :: getEmail)
                .setCaption(ClientPageData.EMAIL);
        grid.addColumn(Client :: getPassportNumber)
                .setCaption(ClientPageData.PASSPORT_NUMBER);
        grid.addColumn(Client :: getStringBanks)
                .setCaption(ClientPageData.BANKS)
                .setResizable(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> {
            selected = new ArrayList<>(event.getAllSelectedItems());
            deleteClientsButton.setEnabled(selected.size() > 0);
            editClientButton.setEnabled(selected.size() == 1);
        });
        addComponents(grid);
    }

    private void configureButtons()
    {
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        Button createClientButton = new Button(ClientPageData.BUTTONS_PANEL_CREATE_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForCreatingOrEditClient(new Client()))
        );
        editClientButton = new Button(ClientPageData.BUTTONS_PANEL_EDIT_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForCreatingOrEditClient(selected.get(0)))
        );
        deleteClientsButton = new Button(ClientPageData.BUTTONS_PANEL_DELETE_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForDeleteClients(selected))
        );
        createClientButton.setEnabled(true);
        editClientButton.setEnabled(false);
        deleteClientsButton.setEnabled(false);
        buttonsPanel.addComponents(createClientButton, editClientButton, deleteClientsButton);
        addComponent(buttonsPanel);
    }

    private Window createWindowForDeleteClients(List<Client> selected)
    {
        Window window = new Window();
        VerticalLayout main = new VerticalLayout();
        Label question = new Label(ClientPageData.DELETE_WINDOW_QUESTION);
        HorizontalLayout answerButtons = new HorizontalLayout();
        Button positiveAnswer = new Button(ClientPageData.DELETE_WINDOW_POSITIVE_ANSWER, clickEvent -> {
            clientService.deleteAll(selected);
            grid.setItems(clientService.getAll());
            getUI().removeWindow(window);
        });
        Button negativeAnswer =
                new Button(ClientPageData.DELETE_WINDOW_NEGATIVE_ANSWER, clickEvent -> getUI().removeWindow(window));
        answerButtons.addComponents(positiveAnswer, negativeAnswer);
        main.addComponents(question, answerButtons);
        window.setContent(main);
        window.setModal(true);
        window.center();
        return window;
    }

    private Window createWindowForCreatingOrEditClient(Client client)
    {
        Window window = new Window();
        VerticalLayout mine = new VerticalLayout();
        TextField name;
        TextField surname;
        TextField patronymic;
        TextField phone;
        TextField email;
        TextField passportNumber;
        if (client.getId() != null)
        {
            name = new TextField(ClientPageData.NAME, client.getName());
            surname = new TextField(ClientPageData.SURNAME, client.getSurname());
            patronymic = new TextField(ClientPageData.PATRONYMIC, client.getPatronymic());
            phone = new TextField(ClientPageData.PHONE, client.getPhone());
            email = new TextField(ClientPageData.EMAIL, client.getEmail());
            passportNumber = new TextField(ClientPageData.PASSPORT_NUMBER, client.getPassportNumber());
        }
        else
        {
            name = new TextField(ClientPageData.NAME);
            surname = new TextField(ClientPageData.SURNAME);
            patronymic = new TextField(ClientPageData.PATRONYMIC);
            phone = new TextField(ClientPageData.PHONE);
            email = new TextField(ClientPageData.EMAIL);
            passportNumber = new TextField(ClientPageData.PASSPORT_NUMBER);
        }
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
                new Notification(ClientPageData.ERROR_NAME_NOTIFICATION,
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
                new Notification(ClientPageData.ERROR_PATRONYMIC_NOTIFICATION,
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
                new Notification(ClientPageData.ERROR_PASSPORT_NUMBER_NOTIFICATION,
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
                client.setClientBanks(bankCheckBox.getSelectedItems());// ошибка тут !
                clientService.save(client);

                grid.setItems(clientService.getAll());
                getUI().removeWindow(window);
            }
        });

        Button cancelButton = new Button(ClientPageData.CANCEL, clickEvent -> getUI().removeWindow(window));
        firstRow.addComponents(name, surname, patronymic);
        secondRow.addComponents(phone, email, passportNumber);
        buttonsRow.addComponents(saveButton, cancelButton);
        VerticalLayout bankLayout = new VerticalLayout();
        bankLayout.addComponents(bankCheckBox);
        bankLayout.setMargin(true);
        mine.addComponents(firstRow, secondRow, bankCheckBox, buttonsRow);
        setCaption("Crating client");
        window.setContent(mine);
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


}
