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
 * The screen the app opens on: top bar with the yellow "Search" button
 * (right), and the read-only 7-day booking calendar underneath.
 *
 * <p>"Search" is the entry point into the booking flow: it looks the
 * customer up by phone number (see {@link CustomerSearchPanel}), then
 * pet selection, room/services selection, and a final review before the
 * booking is saved (see {@link MainFrame}).</p>
 */
public class DashboardPanel extends JPanel {

    private final CalendarPanel calendarPanel;

    public DashboardPanel(RoomController roomController, BookingController bookingController,
            Runnable onSearch) {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        add(createTopBar(onSearch), BorderLayout.NORTH);

        calendarPanel = new CalendarPanel(bookingController, roomController.getAllRooms());
        add(calendarPanel, BorderLayout.CENTER);
    }

    private JPanel createTopBar(Runnable onSearch) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIStyle.COLOR_CARD);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)));

        JButton searchButton = new JButton("New Booking");
        searchButton.setFont(UIStyle.FONT_BUTTON);
        searchButton.setBackground(UIStyle.COLOR_YELLOW);
        searchButton.setForeground(UIStyle.COLOR_YELLOW_TEXT);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(9, 22, 9, 22));
        searchButton.addActionListener(e -> onSearch.run());

        JLabel appTitle = new JLabel("Pet Hotel", javax.swing.SwingConstants.CENTER);
        appTitle.setFont(UIStyle.FONT_TITLE);
        appTitle.setForeground(UIStyle.COLOR_TEXT_DARK);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(searchButton);

        topBar.add(appTitle, BorderLayout.CENTER);
        topBar.add(right, BorderLayout.EAST);
        return topBar;
    }

    public void refreshCalendar() {
        calendarPanel.refresh();
    }
}
