package pethotel.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pethotel.controller.BookingController;
import pethotel.controller.CustomerController;
import pethotel.controller.RoomController;

/**
 * Top-level window. Holds two cards in a CardLayout:
 *  - "DASHBOARD": yellow "Search" button, 7-day calendar below (view only)
 *  - "FLOW": a single container whose content is swapped as the person
 *    moves through the booking flow (Search customer -&gt; Room/Services -&gt;
 *    Review). A small back-stack lets "Back" return to the previous step
 *    instead of always going to the dashboard.
 */
public class MainFrame extends JFrame {

    private static final String CARD_DASHBOARD = "DASHBOARD";
    private static final String CARD_FLOW = "FLOW";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final JPanel flowContainer = new JPanel(new BorderLayout());
    private final Deque<JComponent> backStack = new ArrayDeque<>();

    private final BookingController bookingController;
    private final CustomerController customerController;
    private final RoomController roomController;

    private DashboardPanel dashboardPanel;
    private CustomerSearchPanel searchPanel;

    public MainFrame(BookingController bookingController, CustomerController customerController,
            RoomController roomController) {
        super("Pet Hotel");
        this.bookingController = bookingController;
        this.customerController = customerController;
        this.roomController = roomController;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1050, 700));

        dashboardPanel = new DashboardPanel(roomController, bookingController, this::startSearch);
        searchPanel = new CustomerSearchPanel(customerController, this::showDashboard, this::onCustomerAndPetChosen);

        cards.add(dashboardPanel, CARD_DASHBOARD);
        cards.add(flowContainer, CARD_FLOW);

        add(cards);
        pack();

        cardLayout.show(cards, CARD_DASHBOARD);
    }

    private void showDashboard() {
        dashboardPanel.refreshCalendar();
        backStack.clear();
        cardLayout.show(cards, CARD_DASHBOARD);
    }

    private void startSearch() {
        searchPanel.reset();
        backStack.clear();
        setFlowContent(searchPanel);
    }

    private void onCustomerAndPetChosen(pethotel.model.Customer customer, pethotel.model.Pet pet) {
        RoomServicePanel roomServicePanel = new RoomServicePanel(bookingController, roomController,
                customer, pet, this::goBack, draft -> onRoomServiceChosen(draft));
        setFlowContent(roomServicePanel);
    }

    private void onRoomServiceChosen(BookingDraft draft) {
        ReviewBookingPanel reviewPanel = new ReviewBookingPanel(bookingController, draft,
                this::goBack, this::onBookingCreated);
        setFlowContent(reviewPanel);
    }

    private void onBookingCreated() {
        showDashboard();
    }

    private void goBack() {
        if (backStack.isEmpty()) {
            showDashboard();
            return;
        }
        JComponent previous = backStack.pop();
        showFlowContentWithoutPushing(previous);
    }

    private void setFlowContent(JComponent content) {
        JComponent current = currentFlowContent();
        if (current != null) {
            backStack.push(current);
        }
        showFlowContentWithoutPushing(content);
    }

    private void showFlowContentWithoutPushing(JComponent content) {
        flowContainer.removeAll();
        flowContainer.add(content, BorderLayout.CENTER);
        flowContainer.revalidate();
        flowContainer.repaint();
        cardLayout.show(cards, CARD_FLOW);
    }

    private JComponent currentFlowContent() {
        if (flowContainer.getComponentCount() == 0) {
            return null;
        }
        return (JComponent) flowContainer.getComponent(0);
    }
}
