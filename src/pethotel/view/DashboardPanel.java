package pethotel.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pethotel.controller.BookingController;
import pethotel.controller.RoomController;

/**
 * The screen the app opens on: top bar with the yellow "login" button
 * (right), and the read-only booking calendar underneath.
 *
 * <p>There is no direct "Select Room" entry point here anymore - room
 * selection now only happens after going through the owner/pet flow
 * behind the "Login" button (see {@link MainFrame}).</p>
 */
public class DashboardPanel extends JPanel {

    private final CalendarPanel calendarPanel;

    public DashboardPanel(RoomController roomController, BookingController bookingController,
            Runnable onLogin) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        add(createTopBar(onLogin), BorderLayout.NORTH);

        calendarPanel = new CalendarPanel(bookingController, roomController.getAllRooms());
        add(calendarPanel, BorderLayout.CENTER);
    }

    private JPanel createTopBar(Runnable onLogin) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIStyle.COLOR_CARD);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(UIStyle.FONT_BUTTON);
        loginButton.setBackground(UIStyle.COLOR_YELLOW);
        loginButton.setForeground(UIStyle.COLOR_YELLOW_TEXT);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(9, 22, 9, 22));
        loginButton.addActionListener(e -> onLogin.run());

        JLabel appTitle = new JLabel("Pet Hotel", javax.swing.SwingConstants.CENTER);
        appTitle.setFont(UIStyle.FONT_TITLE);
        appTitle.setForeground(UIStyle.COLOR_TEXT_DARK);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(loginButton);

        topBar.add(appTitle, BorderLayout.CENTER);
        topBar.add(right, BorderLayout.EAST);
        return topBar;
    }

    public void refreshCalendar() {
        calendarPanel.refresh();
    }
}
