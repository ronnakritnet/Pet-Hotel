package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pethotel.controller.BookingController;
import pethotel.model.Booking;
import pethotel.model.Room;

/** Step 3 (final): read-only summary of the whole booking, then "Save Booking". */
public class ReviewBookingPanel extends JPanel {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEE d MMM yyyy");

    private final BookingController bookingController;
    private final BookingDraft draft;
    private final Runnable onBack;
    private final Runnable onConfirmed;

    private JCheckBox confirmCheckBox;

    public ReviewBookingPanel(BookingController bookingController, BookingDraft draft,
            Runnable onBack, Runnable onConfirmed) {
        this.bookingController = bookingController;
        this.draft = draft;
        this.onBack = onBack;
        this.onConfirmed = onConfirmed;

        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIStyle.COLOR_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JButton backButton = new JButton("< Back");
        backButton.setFont(UIStyle.FONT_BUTTON);
        backButton.setForeground(java.awt.Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("Review Booking");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(java.awt.Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Please review all information before saving");
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(new java.awt.Color(226, 225, 253));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(backButton);
        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JScrollPane createBody() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        content.add(sectionCard("Customer Information",
                "Name: " + draft.customer.getName(),
                "Phone: " + draft.customer.getPhoneNumber()));
        content.add(Box.createVerticalStrut(14));

        content.add(sectionCard("Pet Information",
                "Type: " + draft.pet.getPetType(),
                "Name: " + draft.pet.getName(),
                "Breed: " + draft.pet.getBreed(),
                "Weight: " + draft.pet.getWeight() + " kg"));
        content.add(Box.createVerticalStrut(14));

        long nights = java.time.temporal.ChronoUnit.DAYS.between(draft.checkIn, draft.checkOut);
        content.add(sectionCard("Stay Information",
                "Check-in: " + draft.checkIn.format(DATE_FORMAT),
                "Check-out: " + draft.checkOut.format(DATE_FORMAT),
                "Nights: " + nights));
        content.add(Box.createVerticalStrut(14));

        content.add(createRoomDetailCard());
        content.add(Box.createVerticalStrut(14));

        content.add(sectionCard("Selected Services",
                "Dog Walking: " + (draft.walking ? "Yes (+" + (int) BookingController.WALKING_PRICE + " THB)" : "No"),
                "Grooming: " + (draft.grooming ? "Yes (+" + (int) BookingController.GROOMING_PRICE + " THB)" : "No")));
        content.add(Box.createVerticalStrut(14));

        content.add(createPriceSummaryCard());
        content.add(Box.createVerticalStrut(14));

        confirmCheckBox = new JCheckBox("I confirm the information above is correct");
        confirmCheckBox.setFont(UIStyle.FONT_BODY);
        confirmCheckBox.setOpaque(false);
        confirmCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(confirmCheckBox);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel sectionCard(String title, String... lines) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIStyle.FONT_HEADING);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));

        for (String line : lines) {
            JLabel label = new JLabel(line);
            label.setFont(UIStyle.FONT_BODY);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(label);
        }
        return card;
    }

    private JPanel createRoomDetailCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JLabel titleLabel = new JLabel("Room Detail (per night)");
        titleLabel.setFont(UIStyle.FONT_HEADING);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));

        Map<LocalDate, Room> sorted = new TreeMap<>(draft.roomPerNight);
        for (Map.Entry<LocalDate, Room> entry : sorted.entrySet()) {
            JLabel line = new JLabel(entry.getKey().format(DATE_FORMAT) + "  -  " + entry.getValue().getRoomName()
                    + "  (" + (int) entry.getValue().getPricePerNight() + " THB)");
            line.setFont(UIStyle.FONT_BODY);
            line.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(line);
        }
        return card;
    }

    private JPanel createPriceSummaryCard() {
        double roomTotal = 0;
        for (Room room : draft.roomPerNight.values()) {
            roomTotal += room.getPricePerNight();
        }
        double addonTotal = draft.estimatedTotal - roomTotal;

        return sectionCard("Price Summary",
                "Room charges: " + (int) roomTotal + " THB",
                "Add-on services: " + (int) addonTotal + " THB",
                "Total: " + (int) draft.estimatedTotal + " THB");
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UIStyle.COLOR_CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JButton backButton = new JButton("Back");
        backButton.setFont(UIStyle.FONT_BUTTON);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> onBack.run());

        JButton saveButton = new JButton("Save Booking");
        saveButton.setFont(UIStyle.FONT_BUTTON);
        saveButton.setBackground(UIStyle.COLOR_PRIMARY);
        saveButton.setForeground(java.awt.Color.WHITE);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> save());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(backButton);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(saveButton);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    private void save() {
        if (!confirmCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please confirm the information is correct first.");
            return;
        }
        try {
            Booking booking = bookingController.createBooking(draft.customer, draft.pet, draft.roomPerNight,
                    draft.walking, draft.grooming);
            JOptionPane.showMessageDialog(this,
                    "Booking saved! Booking ID: " + booking.getBookingId()
                            + "\nTotal: " + (int) booking.getTotalPrice() + " THB");
            onConfirmed.run();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not save booking: " + ex.getMessage(),
                    "Booking Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
