package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pethotel.controller.BookingController;
import pethotel.model.Room;

/**
 * Visual date-range picker shown after a room is chosen on
 * {@link RoomSelectPanel} and before {@link BookingFormDialog}. Each day
 * button is colored from the real booking data for the selected room
 * (green = available, red = already booked) and turns yellow once picked,
 * matching the calendar look used elsewhere in the app.
 *
 * <p>Pick the check-in day, then the check-out day (a later, available
 * day). Clicking "Next" hands both dates back to the caller via
 * {@code onDatesConfirmed} so they can be pre-filled into the booking
 * form.</p>
 */
public class BookingCalendarDialog extends JDialog {

    private static final int DAYS_SHOWN = 30;
    private static final DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ofPattern("d");
    private static final DateTimeFormatter TOOLTIP_FORMAT = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    private final BookingController bookingController;
    private final Room room;
    private final BiConsumer<LocalDate, LocalDate> onDatesConfirmed;

    private final LocalDate[] dates = new LocalDate[DAYS_SHOWN];
    private final JButton[] dayButtons = new JButton[DAYS_SHOWN];

    private LocalDate selectedCheckIn;
    private LocalDate selectedCheckOut;

    public BookingCalendarDialog(Frame owner, BookingController bookingController, Room room,
            BiConsumer<LocalDate, LocalDate> onDatesConfirmed) {
        super(owner, "Select Booking Dates", true);
        this.bookingController = bookingController;
        this.room = room;
        this.onDatesConfirmed = onDatesConfirmed;

        LocalDate today = LocalDate.now();
        for (int i = 0; i < DAYS_SHOWN; i++) {
            dates[i] = today.plusDays(i);
        }

        setLayout(new BorderLayout());
        setSize(520, 560);
        setLocationRelativeTo(owner);

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        refreshColors();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIStyle.COLOR_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Select Booking Dates");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(java.awt.Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose check-in and check-out for " + room.getRoomName());
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(new java.awt.Color(226, 225, 253));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        content.add(createCalendarCard());
        content.add(Box.createVerticalStrut(12));
        content.add(createLegendRow());

        return content;
    }

    private JPanel createCalendarCard() {
        JPanel card = new JPanel(new GridLayout(0, 7, 5, 5));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        for (int i = 0; i < DAYS_SHOWN; i++) {
            final LocalDate date = dates[i];

            JButton dayButton = new JButton(date.format(DAY_FORMAT));
            dayButton.setFont(UIStyle.FONT_BODY);
            dayButton.setForeground(java.awt.Color.WHITE);
            dayButton.setOpaque(true);
            dayButton.setBorderPainted(false);
            dayButton.setFocusPainted(false);
            dayButton.setPreferredSize(new Dimension(45, 38));
            dayButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            dayButton.setToolTipText(date.format(TOOLTIP_FORMAT));
            dayButton.addActionListener(e -> onDayClicked(date));

            dayButtons[i] = dayButton;
            card.add(dayButton);
        }

        return card;
    }

    private JPanel createLegendRow() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        legend.setOpaque(false);
        legend.setAlignmentX(Component.LEFT_ALIGNMENT);

        legend.add(createLegendItem("Available", UIStyle.COLOR_CAL_AVAILABLE));
        legend.add(createLegendItem("Booked", UIStyle.COLOR_CAL_BOOKED));
        legend.add(createLegendItem("Selected", UIStyle.COLOR_CAL_SELECTED));

        return legend;
    }

    private JPanel createLegendItem(String label, java.awt.Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);

        JLabel dot = new JLabel("\u25CF", SwingConstants.CENTER);
        dot.setForeground(color);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel text = new JLabel(label);
        text.setFont(UIStyle.FONT_SUBTITLE);
        text.setForeground(UIStyle.COLOR_TEXT_DARK);

        item.add(dot);
        item.add(text);
        return item;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UIStyle.COLOR_CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JButton backButton = new JButton("Cancel");
        backButton.setFont(UIStyle.FONT_BUTTON);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());

        JButton nextButton = new JButton("Next >");
        nextButton.setFont(UIStyle.FONT_BUTTON);
        nextButton.setBackground(UIStyle.COLOR_PRIMARY);
        nextButton.setForeground(java.awt.Color.WHITE);
        nextButton.setOpaque(true);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.addActionListener(e -> confirmSelection());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(backButton);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(nextButton);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    private boolean isBooked(LocalDate date) {
        return bookingController.getBookingFor(room, date) != null;
    }

    private void onDayClicked(LocalDate date) {
        if (isBooked(date)) {
            return;
        }

        if (selectedCheckIn == null || selectedCheckOut != null) {
            // Starting a fresh selection.
            selectedCheckIn = date;
            selectedCheckOut = null;
        } else if (date.equals(selectedCheckIn)) {
            // Clicked the same day again - clear the selection.
            selectedCheckIn = null;
        } else if (date.isBefore(selectedCheckIn)) {
            // Picked an earlier day - treat it as the new check-in.
            selectedCheckIn = date;
        } else {
            // Picking the check-out day - make sure no booked day sits in between.
            if (hasBookedDayBetween(selectedCheckIn, date)) {
                JOptionPane.showMessageDialog(this,
                        "That range includes an already booked day. Please choose different dates.",
                        "Unavailable Dates", JOptionPane.WARNING_MESSAGE);
                selectedCheckIn = date;
                selectedCheckOut = null;
            } else {
                selectedCheckOut = date;
            }
        }

        refreshColors();
    }

    private boolean hasBookedDayBetween(LocalDate start, LocalDate end) {
        LocalDate cursor = start.plusDays(1);
        while (cursor.isBefore(end)) {
            if (isBooked(cursor)) {
                return true;
            }
            cursor = cursor.plusDays(1);
        }
        return false;
    }

    private void refreshColors() {
        for (int i = 0; i < DAYS_SHOWN; i++) {
            LocalDate date = dates[i];
            JButton button = dayButtons[i];

            if (isBooked(date)) {
                button.setBackground(UIStyle.COLOR_CAL_BOOKED);
                button.setEnabled(false);
            } else if (isInSelectedRange(date)) {
                button.setBackground(UIStyle.COLOR_CAL_SELECTED);
                button.setEnabled(true);
            } else {
                button.setBackground(UIStyle.COLOR_CAL_AVAILABLE);
                button.setEnabled(true);
            }
        }
    }

    private boolean isInSelectedRange(LocalDate date) {
        if (selectedCheckIn == null) {
            return false;
        }
        if (selectedCheckOut == null) {
            return date.equals(selectedCheckIn);
        }
        return !date.isBefore(selectedCheckIn) && !date.isAfter(selectedCheckOut);
    }

    private void confirmSelection() {
        if (selectedCheckIn == null || selectedCheckOut == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select both a check-in and a check-out date.",
                    "Dates Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        onDatesConfirmed.accept(selectedCheckIn, selectedCheckOut);
        dispose();
    }
}
