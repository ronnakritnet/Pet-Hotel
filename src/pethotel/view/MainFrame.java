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
 *  - "DASHBOARD": yellow "login" button, calendar below (view only)
 *  - "ROOM_SELECT": pick room A/B/C/D, opens the booking form dialog
 *  - "LOGIN": pet-owner lookup screen ({@link OwnerPanel}), reached via the
 *    dashboard's "Login" button; "Register" opens {@link RegisterDialog}
 *    from there.
 *
 * <p>Note: {@link OwnerPanel} looks up owners from a small hardcoded list
 * and {@link RegisterPanel} doesn't persist anywhere yet, since there is
 * no Customer/account model or controller wired up in the project.</p>
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
                this::showLogin);

        RoomSelectPanel roomSelectPanel = new RoomSelectPanel(roomController, bookingController,
                this::showDashboard, this::onBookingCreated);

        OwnerPanel ownerPanel = new OwnerPanel(this::showDashboard,
                () -> showRoomSelect(bookingController, roomController));

        cards.add(dashboardPanel, CARD_DASHBOARD);
        cards.add(roomSelectPanel, CARD_ROOM_SELECT);
        cards.add(ownerPanel, CARD_LOGIN);

        add(cards);
        pack();

        cardLayout.show(cards, CARD_DASHBOARD);
    }

    private void showDashboard() {
        dashboardPanel.refreshCalendar();
        cardLayout.show(cards, CARD_DASHBOARD);
    }

    private void showRoomSelect(BookingController bookingController, RoomController roomController) {
        cardLayout.show(cards, CARD_ROOM_SELECT);
    }

    private void showLogin() {
        cardLayout.show(cards, CARD_LOGIN);
    }

    private void onBookingCreated() {
        dashboardPanel.refreshCalendar();
        showDashboard();
    }
}
