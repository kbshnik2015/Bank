package bank.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@SpringView (name = "")
public class LoanProcessing extends VerticalLayout implements View, Serializable
{
    @PostConstruct
    void init()
    {
        addComponent(new Label("Apply for a loan"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {

    }

}
