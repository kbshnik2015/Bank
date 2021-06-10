package bank.views.clientView;


import bank.entity.Client;
import bank.services.ClientService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@SpringView(name = "Clients")
public class ClientPage  extends VerticalLayout implements View, Serializable
{
    @Autowired
    ClientService clientService;
    private final TextField name = new TextField("name");
    private final TextField surname = new TextField("surname");
    private final TextField patronymic = new TextField("patronymic");
    private final TextField phone = new TextField("phone");
    private final TextField email = new TextField("email");
    private final TextField passportNumber = new TextField("passportNumber");

    @PostConstruct
    void init() {
        addComponent(name);
        addComponent(surname);
        addComponent(patronymic);
        addComponent(phone);
        addComponent(email);
        addComponent(passportNumber);
        Button createButton = new Button("Create client");
        createButton.addClickListener(clickEvent -> this.save());
        addComponent(createButton);
    }

    private void save()
    {
        Client client = new Client();
        client.setName(name.getValue());
        client.setSurname(surname.getValue());
        client.setPatronymic(patronymic.getValue());
        client.setPhone(phone.getValue());
        client.setEmail(email.getValue());
        client.setPassportNumber(passportNumber.getValue());
        clientService.save(client);
        Notification notification = new Notification("Успешно! Клиент добавлен",
                Notification.Type.HUMANIZED_MESSAGE);
        notification.setDelayMsec(1500);
        notification.show(getUI().getPage());
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }
}
