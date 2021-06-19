package bank.views;


import bank.entity.Credit;
import bank.interfaces.dataPage.CreditPageData;
import bank.interfaces.regExp.CreditRegExp;
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

@SpringView (name = "Credits")
public class CreditPage extends VerticalLayout implements View, Serializable
{
    @Autowired
    CreditService creditService;

    private Grid<Credit> grid;
    private Button editCreditButton;
    private Button deleteCreditButton;
    private List<Credit> selected;

    @PostConstruct
    void init()
    {
        configureButtons();
        configureGrid();

    }


    private void configureGrid()
    {
        grid = new Grid<>(Credit.class);
        grid.setItems(creditService.getAll());
        grid.removeAllColumns();
        grid.setWidth("100%");
        grid.addColumn(Credit :: getLimit)
                .setCaption(CreditPageData.LIMIT);
        grid.addColumn(Credit :: getPercent)
                .setCaption(CreditPageData.PERCENT);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> {
            selected = new ArrayList<>(event.getAllSelectedItems());
            deleteCreditButton.setEnabled(selected.size() > 0);
            editCreditButton.setEnabled(selected.size() == 1);
        });
        addComponents(grid);
    }

    private void configureButtons()
    {
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        Button createCreditButton = new Button(CreditPageData.BUTTONS_PANEL_CREATE_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForCreatingOrEditCredit(new Credit()))
        );
        editCreditButton = new Button(CreditPageData.BUTTONS_PANEL_EDIT_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForCreatingOrEditCredit(selected.get(0)))
        );
        deleteCreditButton = new Button(CreditPageData.BUTTONS_PANEL_DELETE_BUTTON,
                clickEvent -> getUI().addWindow(createWindowForDeleteClients(selected))
        );
        createCreditButton.setEnabled(true);
        editCreditButton.setEnabled(false);
        deleteCreditButton.setEnabled(false);
        buttonsPanel.addComponents(createCreditButton, editCreditButton, deleteCreditButton);
        addComponent(buttonsPanel);
    }

    private Window createWindowForCreatingOrEditCredit(Credit credit)
    {
        Window window = new Window();
        VerticalLayout mine = new VerticalLayout();
        TextField limit;
        TextField percent;

        if (credit.getId() != null)
        {
            limit = new TextField(CreditPageData.LIMIT, String.valueOf(credit.getLimit()));
            percent = new TextField(CreditPageData.PERCENT, String.valueOf(credit.getPercent()));
        }
        else
        {
            limit = new TextField(CreditPageData.LIMIT);
            percent = new TextField(CreditPageData.PERCENT);
        }
        limit.setRequiredIndicatorVisible(true);
        percent.setRequiredIndicatorVisible(true);
        HorizontalLayout buttonsRow = new HorizontalLayout();
        Button saveButton = new Button(CreditPageData.SAVE_BUTTON, clickEvent -> {
            if (!isValidLimit(limit.getValue()))
            {
                new Notification(
                        CreditPageData.ERROR_LIMIT_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }
            else if (!isValidPercent(percent.getValue()))
            {
                new Notification(
                        CreditPageData.ERROR_PERCENT_NOTIFICATION,
                        Notification.Type.ERROR_MESSAGE
                ).show(getUI().getPage());
            }
            else
            {
                credit.setLimit(Double.parseDouble(limit.getValue()));
                credit.setPercent(Double.parseDouble(percent.getValue()));
                if (credit.getId() != null)
                {
                    creditService.update(credit);
                }
                else
                {
                    creditService.save(credit);
                }
                grid.setItems(creditService.getAll());
                getUI().removeWindow(window);
            }
        });

        Button cancelButton = new Button(CreditPageData.CANCEL, clickEvent -> getUI().removeWindow(window));
        buttonsRow.addComponents(saveButton, cancelButton);
        mine.addComponents(limit, percent, buttonsRow);
        setCaption("Crating credit");
        window.setContent(mine);
        window.setModal(true);
        window.center();
        return window;
    }

    private Window createWindowForDeleteClients(List<Credit> selected)
    {
        Window window = new Window();
        VerticalLayout main = new VerticalLayout();
        Label question = new Label(CreditPageData.DELETE_WINDOW_QUESTION);
        HorizontalLayout answerButtons = new HorizontalLayout();
        Button positiveAnswer = new Button(CreditPageData.DELETE_WINDOW_POSITIVE_ANSWER, clickEvent -> {
            creditService.deleteAll(selected);
            grid.setItems(creditService.getAll());
            getUI().removeWindow(window);
        });
        Button negativeAnswer =
                new Button(CreditPageData.DELETE_WINDOW_NEGATIVE_ANSWER, clickEvent -> getUI().removeWindow(window));
        answerButtons.addComponents(positiveAnswer, negativeAnswer);
        main.addComponents(question, answerButtons);
        window.setContent(main);
        window.setModal(true);
        window.center();
        return window;
    }

    private boolean isValidPercent(String percent)
    {
        return percent.matches(CreditRegExp.PERCENT_REGEXP);
    }

    private boolean isValidLimit(String limit)
    {
        return limit.matches(CreditRegExp.LIMIT_REGEXP);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {

    }
}
