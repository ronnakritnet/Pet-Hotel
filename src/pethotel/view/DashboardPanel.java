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
 * The screen the app opens on: top bar with the red "select room" button
 * (left) and the yellow "login" button (right), and the read-only booking
 * calendar underneath.
 *
 * <p>The red "Select Room" button is hidden on the very first screen -
 * it only becomes visible/usable once staff have logged in successfully
 * (see {@link #setRoomSelectAvailable(boolean)}, called from
 * {@link MainFrame} after a successful login).</p>
 */
public class DashboardPanel extends JPanel {

    private final CalendarPanel calendarPanel;
    private final JButton selectRoomButton;

    public DashboardPanel(RoomController roomController, BookingController bookingController,
            Runnable onSelectRoom, Runnable onLogin) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIStyle.COLOR_CARD);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)));

        selectRoomButton = new JButton("Select Room");
        selectRoomButton.setFont(UIStyle.FONT_BUTTON);
        selectRoomButton.setBackground(UIStyle.COLOR_RED);
        selectRoomButton.setForeground(java.awt.Color.WHITE);
        selectRoomButton.setOpaque(true);
        selectRoomButton.setBorderPainted(false);
        selectRoomButton.setFocusPainted(false);
        selectRoomButton.setBorder(BorderFactory.createEmptyBorder(9, 22, 9, 22));
        selectRoomButton.addActionListener(e -> onSelectRoom.run());
        // Hidden until the staff member has logged in successfully.
        selectRoomButton.setVisible(false);

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

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(selectRoomButton);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(loginButton);

        topBar.add(left, BorderLayout.WEST);
        topBar.add(appTitle, BorderLayout.CENTER);
        topBar.add(right, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        calendarPanel = new CalendarPanel(bookingController, roomController.getAllRooms());
        add(calendarPanel, BorderLayout.CENTER);
    }

    /** Call with {@code true} right after a successful login to reveal the "Select Room" button. */
    public void setRoomSelectAvailable(boolean available) {
        selectRoomButton.setVisible(available);
    }

    public void refreshCalendar() {
        calendarPanel.refresh();
    }
}
