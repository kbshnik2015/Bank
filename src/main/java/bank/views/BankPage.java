package bank.views;

import bank.entity.Bank;
import bank.entity.Client;
import bank.entity.Credit;
import bank.interfaces.dataPage.BankPageData;
import bank.interfaces.dataPage.ClientPageData;
import bank.interfaces.dataPage.CreditPageData;
import bank.interfaces.regExp.BankRegExp;
import bank.services.BankService;
import bank.services.CreditService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SpringView (name = "Banks")
public class BankPage extends VerticalLayout implements View, Serializable
{
    @Autowired
    BankService bankService;

    @Autowired
    CreditService creditService;

    private Grid<Bank> grid;
    private Button editBankButton;
    private Button deleteBankButton;
    private Button showListOfClients;
    private Button showListOfCredits;
    private List<Bank> selected;


    //TODO: изменить валидатор имени банка
    @PostConstruct
    void init()
    {
        configureButtons();
        configureGrid();

    }


    private void configureGrid()
    {
        grid = new Grid<>(Bank.class);
        grid.setItems(bankService.getAll());
        grid.removeAllColumns();
        grid.setWidth("100%");
        grid.addColumn(Bank :: getName)
                .setCaption(BankPageData.NAME);
        grid.addColumn(Bank :: getClientsString)
                .setCaption(BankPageData.CLIENTS);
        grid.addColumn(Bank :: getCreditsString)
                .setCaption(BankPageData.CREDITS);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> {
            selected = new ArrayList<>(event.getAllSelectedItems());
            deleteBankButton.setEnabled(selected.size() > 0);
            editBankButton.setEnabled(selected.size() == 1);
            showListOfClients.setEnabled(selected.size() == 1);
            showListOfCredits.setEnabled(selected.size() == 1);
        });
        addComponents(grid);
    }

    private void configureButtons()
    {
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        Button createCreditButton = new Button(BankPageData.BUTTONS_PANEL_CREATE_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForCreatingOrEditBank(new Bank()))
        );
        editBankButton = new Button(BankPageData.BUTTONS_PANEL_EDIT_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForCreatingOrEditBank(selected.get(0)))
        );
        deleteBankButton = new Button(BankPageData.BUTTONS_PANEL_DELETE_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForDeleteBanks(selected))
        );
        showListOfClients = new Button(BankPageData.SHOW_CLIENTS_PANEL_BUTTON, clickEvent -> getUI().addWindow(createWindowForClientsList(selected.get(0))));
        showListOfCredits = new Button(BankPageData.SHOW_CREDITS_PANEL_BUTTON, clickEvent -> getUI().addWindow(createWindowForCreditList(selected.get(0))));
        createCreditButton.setEnabled(true);
        editBankButton.setEnabled(false);
        deleteBankButton.setEnabled(false);
        showListOfClients.setEnabled(false);
        showListOfCredits.setEnabled(false);
        buttonsPanel.addComponents(createCreditButton, editBankButton, deleteBankButton, showListOfClients, showListOfCredits);
        addComponent(buttonsPanel);
    }

    private Window createWindowForCreditList(Bank bank)
    {
        Window window = new Window();
        Grid<Credit> creditGrid = new Grid<>();
        HorizontalLayout main = new HorizontalLayout();
        creditGrid.setItems(bank.getCredits());
        creditGrid.removeAllColumns();
        creditGrid.setWidth("100%");
        creditGrid.addColumn(Credit :: getLimit)
                .setCaption(CreditPageData.LIMIT);
        creditGrid.addColumn(Credit :: getPercent)
                .setCaption(CreditPageData.PERCENT);
        main.addComponents(creditGrid);
        window.setContent(main);
        window.setCaption(BankPageData.CREDITS_LIST_WINDOW);
        window.setModal(true);
        window.center();
        return window;
    }

    private Window createWindowForClientsList(Bank bank)
    {
        Window window = new Window();
        Grid<Client> clientsGrid = new Grid<>();
        HorizontalLayout main = new HorizontalLayout();
        clientsGrid.setItems(bank.getClients());
        clientsGrid.removeAllColumns();
        clientsGrid.setWidth("100%");
        clientsGrid.addColumn(Client :: getName)
                .setCaption(ClientPageData.NAME);
        clientsGrid.addColumn(Client :: getSurname)
                .setCaption(ClientPageData.SURNAME);
        clientsGrid.addColumn(Client :: getPatronymic)
                .setCaption(ClientPageData.PATRONYMIC);
        clientsGrid.addColumn(Client :: getPhone)
                .setCaption(ClientPageData.PHONE);
        clientsGrid.addColumn(Client :: getEmail)
                .setCaption(ClientPageData.EMAIL);
        clientsGrid.addColumn(Client :: getPassportNumber)
                .setCaption(ClientPageData.PASSPORT_NUMBER);
        main.addComponents(clientsGrid);
        window.setContent(main);
        window.setCaption(BankPageData.CLIENTS_LIST_WINDOW);
        window.setModal(true);
        window.center();
        return window;
    }

    private Window createWindowForCreatingOrEditBank(Bank bank)
    {
        Window window = new Window();
        VerticalLayout mine = new VerticalLayout();
        TextField name;


        if (bank.getId() != null)
        {
            name = new TextField(BankPageData.NAME, String.valueOf(bank.getName()));
        }
        else
        {
            name = new TextField(BankPageData.NAME);
        }
        name.setRequiredIndicatorVisible(true);
        CheckBoxGroup<Credit> creditCheckBox = new CheckBoxGroup<>("Chose banks", creditService.getAll());
        creditCheckBox.setItemCaptionGenerator(item -> "Limit: " +item.getLimit()+", percent: "+item.getPercent()+";");
        HorizontalLayout buttonsRow = new HorizontalLayout();
        Button saveButton = new Button(BankPageData.SAVE_BUTTON, clickEvent -> {
            if (!isValidName(name.getValue()))
            {
                new Notification(
                        BankPageData.ERROR_NAME_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }else
            {
                bank.setName(name.getValue());
                bank.setCredits(creditCheckBox.getSelectedItems());
                bankService.save(bank);

                grid.setItems(bankService.getAll());
                getUI().removeWindow(window);
            }
        });

        Button cancelButton = new Button(BankPageData.CANCEL, clickEvent -> getUI().removeWindow(window));
        buttonsRow.addComponents(saveButton, cancelButton);
        mine.addComponents(name,creditCheckBox, buttonsRow);
        setCaption("Crating bank");
        window.setContent(mine);
        window.setModal(true);
        window.center();
        return window;
    }

    private Window createWindowForDeleteBanks(List<Bank> selected)
    {
        Window window = new Window();
        VerticalLayout main = new VerticalLayout();
        Label question = new Label(BankPageData.DELETE_WINDOW_QUESTION);
        HorizontalLayout answerButtons = new HorizontalLayout();
        Button positiveAnswer = new Button(BankPageData.DELETE_WINDOW_POSITIVE_ANSWER, clickEvent -> {
            bankService.deleteAll(selected);
            grid.setItems(bankService.getAll());
            getUI().removeWindow(window);
        });
        Button negativeAnswer =
                new Button(BankPageData.DELETE_WINDOW_NEGATIVE_ANSWER, clickEvent -> getUI().removeWindow(window));
        answerButtons.addComponents(positiveAnswer, negativeAnswer);
        main.addComponents(question, answerButtons);
        window.setContent(main);
        window.setModal(true);
        window.center();
        return window;
    }

    private boolean isValidName(String limit)
    {
        return limit.matches(BankRegExp.NAME_REGEXP);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {

    }
}

