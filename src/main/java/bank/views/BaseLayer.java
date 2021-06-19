package bank.views;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.io.Serializable;

@Theme ("valo")
@SpringUI
@SpringViewDisplay
public class BaseLayer extends UI implements ViewDisplay, Serializable
{
    final VerticalLayout root = new VerticalLayout();
    private Panel springViewDisplay;

    @Override
    protected void init(VaadinRequest vaadinRequest)
    {
        root.setSizeFull();
        setContent(root);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponent(createNavigationButton("Apply for a loan",""));
        buttons.addComponent(createNavigationButton("Credits","Credits"));
        buttons.addComponent(createNavigationButton("Clients","Clients"));
        buttons.addComponent(createNavigationButton("Banks","Banks"));
        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();

        root.addComponent(buttons);
        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);

    }

    private Button createNavigationButton(String caption, final String path) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_LARGE);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(path));
        return button;
    }

    @Override
    public void showView(View view)
    {
        springViewDisplay.setContent((Component) view);
    }
}
