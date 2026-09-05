package pethotel.view;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pethotel.controller.BookingController;
import pethotel.controller.CustomerController;
import pethotel.controller.RoomController;

/**
 * Top-level window. Holds three screens in a CardLayout:
 *  - "DASHBOARD": red "select room" button (hidden until login) + yellow
 *    "login" button, calendar below (view only)
 *  - "ROOM_SELECT": pick room A/B/C/D, opens the booking form dialog
 *  - "LOGIN": staff login screen (phone number + password)
 *
 * <p>The red "Select Room" button does not appear on the dashboard at all
 * until staff log in successfully - {@link #onLoginSuccess()} reveals it
 * on {@link DashboardPanel} once the login screen accepts the
 * credentials.</p>
 */
public class MainFrame extends JFrame {

    private static final String CARD_DASHBOARD = "DASHBOARD";
    private static final String CARD_ROOM_SELECT = "ROOM_SELECT";
    private static final String CARD_LOGIN = "LOGIN";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private DashboardPanel dashboardPanel;

    public MainFrame(BookingController bookingController, CustomerController customerController,
            RoomController roomController) {
        super("Pet Hotel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 650));

        dashboardPanel = new DashboardPanel(roomController, bookingController,
                this::showRoomSelect,
                this::showLogin);

        RoomSelectPanel roomSelectPanel = new RoomSelectPanel(roomController, bookingController,
                this::showDashboard, this::onBookingCreated);

        LoginPanel loginPanel = new LoginPanel(this::showDashboard, this::onLoginSuccess);

        cards.add(dashboardPanel, CARD_DASHBOARD);
        cards.add(roomSelectPanel, CARD_ROOM_SELECT);
        cards.add(loginPanel, CARD_LOGIN);

        add(cards);
        pack();

        cardLayout.show(cards, CARD_DASHBOARD);
    }

    private void showDashboard() {
        dashboardPanel.refreshCalendar();
        cardLayout.show(cards, CARD_DASHBOARD);
    }

    private void showRoomSelect() {
        cardLayout.show(cards, CARD_ROOM_SELECT);
    }

    private void showLogin() {
        cardLayout.show(cards, CARD_LOGIN);
    }

    /** Called once the login screen accepts valid credentials - reveal the Select Room button. */
    private void onLoginSuccess() {
        dashboardPanel.setRoomSelectAvailable(true);
        showDashboard();
    }

    private void onBookingCreated() {
        dashboardPanel.refreshCalendar();
        showDashboard();
    }
}
