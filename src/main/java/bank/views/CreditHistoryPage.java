package bank.views;

import bank.entity.CreditOffer;
import bank.entity.OneTimePayment;
import bank.interfaces.dataPage.ClientPageData;
import bank.interfaces.dataPage.CreditHistoryPageData;
import bank.services.CreditOfferService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringView (name = "Credit_history")
public class CreditHistoryPage extends VerticalLayout implements View
{
    @Autowired
    CreditOfferService creditOfferService;

    private Grid<CreditOffer> grid;
    private Button showPaymentSchedule;
    private Button deleteCreditOfferButton;
    private List<CreditOffer> selected;



    @PostConstruct
    void init()
    {
        configureButtons();
        configureGrid();

    }

    private void configureGrid()
    {
        grid = new Grid<>(CreditOffer.class);
        grid.setItems(creditOfferService.getAll());
        grid.removeAllColumns();
        grid.setWidth("100%");
        grid.addColumn(CreditOffer :: getClient)
                .setCaption(CreditHistoryPageData.CLIENT);
        grid.addColumn(CreditOffer :: getCredit)
                .setCaption(CreditHistoryPageData.CREDIT);
        grid.addColumn(CreditOffer :: getCreditAmount)
                .setCaption(CreditHistoryPageData.CREDIT_AMOUNT);

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(event -> {
            selected = new ArrayList<>(event.getAllSelectedItems());
            deleteCreditOfferButton.setEnabled(selected.size() > 0);
            showPaymentSchedule.setEnabled(selected.size() == 1);
        });
        addComponents(grid);
    }

    private void configureButtons()
    {
        HorizontalLayout buttonsPanel = new HorizontalLayout();
        showPaymentSchedule = new Button(CreditHistoryPageData.SHOW_PAYMENT_SCHEDULE,
                clickEvent -> getUI().addWindow(createPaymentScheduleWindow())
        );
        showPaymentSchedule.setStyleName(ValoTheme.BUTTON_PRIMARY);
        deleteCreditOfferButton = new Button(CreditHistoryPageData.DELETE,
                clickEvent -> getUI().addWindow(createWindowForDeleteCreditOffers(selected))
        );
        deleteCreditOfferButton.setStyleName(ValoTheme.BUTTON_DANGER);
        deleteCreditOfferButton.setIcon(VaadinIcons.MINUS);
        deleteCreditOfferButton.setStyleName(ValoTheme.BUTTON_DANGER);
        showPaymentSchedule.setEnabled(false);
        deleteCreditOfferButton.setEnabled(false);
        buttonsPanel.addComponents(deleteCreditOfferButton, showPaymentSchedule);
        addComponent(buttonsPanel);
    }

    private Window createPaymentScheduleWindow()
    {
        Window window = new Window();
        Grid<OneTimePayment> paymentScheduleGrid = new Grid<>();
        HorizontalLayout main = new HorizontalLayout();
        paymentScheduleGrid.setItems(selected.get(0).getPaymentSchedule());
        paymentScheduleConfig(paymentScheduleGrid);
        main.addComponents(paymentScheduleGrid);
        window.setContent(main);
        window.setModal(true);
        window.center();
        return window;
    }

    static void paymentScheduleConfig(Grid<OneTimePayment> paymentScheduleGrid)
    {
        paymentScheduleGrid.removeAllColumns();
        paymentScheduleGrid.setWidth("100%");
        paymentScheduleGrid.addColumn(OneTimePayment :: getPayDay)
                .setCaption(CreditHistoryPageData.PAY_DAY);
        paymentScheduleGrid.addColumn(OneTimePayment :: getAmountOfPayment)
                .setCaption(CreditHistoryPageData.PAYMENT_AMOUNT);
        paymentScheduleGrid.addColumn(OneTimePayment :: getLoanBodyAmount )
                .setCaption(CreditHistoryPageData.LOAN_BODY);
        paymentScheduleGrid.addColumn(OneTimePayment :: getInterestRepaymentAmount)
                .setCaption(CreditHistoryPageData.LOAN_PERCENT);
    }

    private Window createWindowForDeleteCreditOffers(List<CreditOffer> selected)
    {
        Window window = new Window();
        VerticalLayout main = new VerticalLayout();
        Label question = new Label(CreditHistoryPageData.DELETE_WINDOW_QUESTION);
        HorizontalLayout answerButtons = new HorizontalLayout();
        Button positiveAnswer = new Button(CreditHistoryPageData.POSETIVE_ANSVER, clickEvent -> {
            creditOfferService.deleteAll(selected);
            grid.setItems(creditOfferService.getAll());
            getUI().removeWindow(window);
        });
        positiveAnswer.setStyleName(ValoTheme.BUTTON_DANGER);
        Button negativeAnswer = new Button(CreditHistoryPageData.CENCEL, clickEvent -> getUI().removeWindow(window));
        negativeAnswer.setStyleName(ValoTheme.BUTTON_PRIMARY);
        answerButtons.addComponents(positiveAnswer, negativeAnswer);
        main.addComponents(question, answerButtons);
        window.setContent(main);
        window.setWidth("35%");
        window.setModal(true);
        window.center();
        return window;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {

    }

}
