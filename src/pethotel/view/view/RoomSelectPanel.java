package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pethotel.controller.BookingController;
import pethotel.controller.RoomController;
import pethotel.model.Room;

/**
 * Screen for picking one of the rooms (A, B, C, D...). Selecting a room
 * opens {@link BookingCalendarDialog} first so the customer can visually
 * pick check-in/check-out dates against that room's real availability,
 * then {@link BookingFormDialog} to collect guest/pet details.
 */
public class RoomSelectPanel extends JPanel {

    private final RoomController roomController;
    private final BookingController bookingController;
    private final Runnable onBack;
    private final Runnable onBookingCreated;

    public RoomSelectPanel(RoomController roomController, BookingController bookingController,
            Runnable onBack, Runnable onBookingCreated) {
        this.roomController = roomController;
        this.bookingController = bookingController;
        this.onBack = onBack;
        this.onBookingCreated = onBookingCreated;

        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createRoomGrid(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIStyle.COLOR_RED);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JButton backButton = new JButton("< Back");
        backButton.setFont(UIStyle.FONT_BUTTON);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("Select a Room");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(java.awt.Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose Room A, B, C, or D to make a booking");
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(new java.awt.Color(255, 226, 226));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(backButton);
        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JPanel createRoomGrid() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIStyle.COLOR_BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        List<Room> rooms = roomController.getAllRooms();
        JPanel grid = new JPanel(new GridLayout(0, 2, 15, 15));
        grid.setBackground(UIStyle.COLOR_BACKGROUND);

        for (Room room : rooms) {
            grid.add(createRoomCard(room));
        }

        wrapper.add(grid, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel createRoomCard(Room room) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        JLabel name = new JLabel(room.getRoomName());
        name.setFont(UIStyle.FONT_HEADING);
        name.setForeground(UIStyle.COLOR_TEXT_DARK);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel type = new JLabel("Type: " + (room.getRoomType().equals("DOG_ROOM") ? "Dog Room" : "Cat Room"));
        type.setFont(UIStyle.FONT_SUBTITLE);
        type.setForeground(UIStyle.COLOR_TEXT_DARK);
        type.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel price = new JLabel(String.format("Price: %.0f THB/night (Max weight %.0f kg)",
                room.getPricePerNight(), room.getMaxWeightLimit()));
        price.setFont(UIStyle.FONT_SUBTITLE);
        price.setForeground(UIStyle.COLOR_TEXT_DARK);
        price.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton selectButton = new JButton("Book This Room");
        selectButton.setFont(UIStyle.FONT_BUTTON);
        selectButton.setBackground(UIStyle.COLOR_RED);
        selectButton.setForeground(java.awt.Color.WHITE);
        selectButton.setOpaque(true);
        selectButton.setBorderPainted(false);
        selectButton.setFocusPainted(false);
        selectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectButton.addActionListener(e -> openBookingCalendar(room));

        card.add(name);
        card.add(Box.createVerticalStrut(6));
        card.add(type);
        card.add(Box.createVerticalStrut(2));
        card.add(price);
        card.add(Box.createVerticalStrut(12));
        card.add(selectButton);
        return card;
    }

    private void openBookingCalendar(Room room) {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);

        BookingCalendarDialog calendarDialog = new BookingCalendarDialog(owner, bookingController, room,
                (checkIn, checkOut) -> {
                    BookingFormDialog formDialog = new BookingFormDialog(owner, bookingController, room,
                            checkIn, checkOut, onBookingCreated);
                    formDialog.setVisible(true);
                });
        calendarDialog.setVisible(true);
    }
}
