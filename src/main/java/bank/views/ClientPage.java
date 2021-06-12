package bank.views;


import bank.entity.Client;
import bank.services.ClientService;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringView (name = "Clients")
public class ClientPage extends VerticalLayout implements View
{
    //TODO:добавить валидацию при создании и редоктировании клиента
    //TODO:добавить ячйки фильтрации в таблицу (поля для фильтрации в каждом поле и кнопку фильтер)

    @Autowired
    private ClientService clientService;

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
                .setCaption("name");
        grid.addColumn(Client :: getSurname)
                .setCaption("surname");
        grid.addColumn(Client :: getPatronymic)
                .setCaption("patronymic");
        grid.addColumn(Client :: getPhone)
                .setCaption("phone number");
        grid.addColumn(Client :: getEmail)
                .setCaption("email");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(Client :: getPassportNumber)
                .setCaption("passport number");

        grid.addSelectionListener(event -> {
            selected = new ArrayList<>(event.getAllSelectedItems());
            deleteClientsButton.setEnabled(CollectionUtils.isEmpty(selected));
            editClientButton.setEnabled(selected.size() == 1);
        });
        addComponents(grid);
    }

    private void configureButtons()
    {
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        Button createClientButton = new Button(
                "Create client",
                clickEvent -> getUI().addWindow(createWindowForCreatingOrEditClient(new Client()))
        );
        editClientButton = new Button(
                "Edit client",
                clickEvent -> getUI().addWindow(createWindowForCreatingOrEditClient(selected.get(0)))
        );
        deleteClientsButton =
                new Button("Delete client", clickEvent -> getUI().addWindow(createWindowForDeleteClients(selected)));
        createClientButton.setEnabled(true);
        editClientButton.setEnabled(false);
        deleteClientsButton.setEnabled(false);
        buttonsPanel.addComponents(createClientButton, editClientButton, deleteClientsButton);
        addComponent(buttonsPanel);
    }

    private Window createWindowForDeleteClients(List<Client> selected)
    {
        Window window = new Window();
        VerticalLayout mine = new VerticalLayout();
        Label ask = new Label("Are you sure you want to delete the selected clients?");
        HorizontalLayout answerButtons = new HorizontalLayout();
        Button yes = new Button("Yes", clickEvent -> {
            clientService.deleteAll(selected);
            grid.setItems(clientService.getAll());
            getUI().removeWindow(window);
        });
        Button cancel = new Button("cancel", clickEvent -> getUI().removeWindow(window));
        answerButtons.addComponents(yes, cancel);
        mine.addComponents(ask, answerButtons);
        window.setContent(mine);
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
            name = new TextField("Name", client.getName());
            surname = new TextField("Surname", client.getSurname());
            patronymic = new TextField("Patronymic", client.getPatronymic());
            phone = new TextField("Phone number", client.getPhone());
            email = new TextField("E-mail", client.getEmail());
            passportNumber = new TextField("Passport number", client.getPassportNumber());
        }
        else
        {
            name = new TextField("Name");
            surname = new TextField("Surname");
            patronymic = new TextField("Patronymic");
            phone = new TextField("Phone number");
            email = new TextField("E-mail");
            passportNumber = new TextField("Passport number");
        }

        HorizontalLayout firstRow = new HorizontalLayout();
        HorizontalLayout secondRow = new HorizontalLayout();
        HorizontalLayout buttonsRow = new HorizontalLayout();
        Button saveButton = new Button("save", clickEvent -> {
            client.setName(name.getValue());
            client.setSurname(surname.getValue());
            client.setPatronymic(patronymic.getValue());
            client.setPhone(phone.getValue());
            client.setEmail(email.getValue());
            client.setPassportNumber(passportNumber.getValue());
            if (client.getId() != null)
            {
                clientService.update(client);
            }
            else
            {
                clientService.save(client);
            }
            grid.setItems(clientService.getAll());
            getUI().removeWindow(window);
        });
        Button cancelButton = new Button("cancel", clickEvent -> getUI().removeWindow(window));
        firstRow.addComponents(name, surname, patronymic);
        secondRow.addComponents(phone, email, passportNumber);
        buttonsRow.addComponents(saveButton, cancelButton);
        mine.addComponents(firstRow, secondRow, buttonsRow);
        setCaption("Crating client");
        window.setContent(mine);
        window.setModal(true);
        window.center();
        return window;
    }


}
