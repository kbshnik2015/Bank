package bank.views.creditsView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@SpringView (name = "")
public class CreditPage extends VerticalLayout implements View, Serializable
{
    @PostConstruct
    void init() {
        Page.getCurrent().setTitle("Credit");
        addComponent(new Label("Credits"));
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {

    }
}
