package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pethotel.controller.BookingController;
import pethotel.model.Booking;
import pethotel.model.Room;

/**
 * Simple form used to create a booking for the room chosen on
 * {@link RoomSelectPanel}. Styled to match ServicePanel (header / card /
 * footer with Back-Next style buttons).
 *
 * <p>The check-in / check-out fields can be pre-filled from
 * {@link BookingCalendarDialog} (the visual date picker shown right before
 * this form), but they stay editable here in case the customer wants to
 * fine-tune the dates.</p>
 */
public class BookingFormDialog extends JDialog {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final BookingController bookingController;
    private final Room room;
    private final Runnable onBookingCreated;

    private JTextField customerNameField;
    private JTextField phoneField;
    private JComboBox<String> petTypeCombo;
    private JTextField petNameField;
    private JTextField breedField;
    private JTextField weightField;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JCheckBox walkingCheckBox;
    private JCheckBox groomingCheckBox;

    /** Original constructor - defaults check-in/check-out to today/tomorrow. */
    public BookingFormDialog(java.awt.Frame owner, BookingController bookingController, Room room,
            Runnable onBookingCreated) {
        this(owner, bookingController, room, LocalDate.now(), LocalDate.now().plusDays(1), onBookingCreated);
    }

    /** Preferred constructor - used after the customer picks dates on {@link BookingCalendarDialog}. */
    public BookingFormDialog(java.awt.Frame owner, BookingController bookingController, Room room,
            LocalDate initialCheckIn, LocalDate initialCheckOut, Runnable onBookingCreated) {
        super(owner, "Book " + room.getRoomName(), true);
        this.bookingController = bookingController;
        this.room = room;
        this.onBookingCreated = onBookingCreated;

        setLayout(new BorderLayout());
        setSize(460, 560);
        setLocationRelativeTo(owner);

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(initialCheckIn, initialCheckOut), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIStyle.COLOR_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Booking Form - " + room.getRoomName());
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(java.awt.Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter the customer and pet details");
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(new java.awt.Color(226, 225, 253));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JPanel createForm(LocalDate initialCheckIn, LocalDate initialCheckOut) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel card = new JPanel(new GridLayout(0, 2, 10, 10));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        customerNameField = new JTextField();
        phoneField = new JTextField();
        petTypeCombo = new JComboBox<>(new String[]{"DOG", "CAT"});
        petNameField = new JTextField();
        breedField = new JTextField();
        weightField = new JTextField();
        checkInField = new JTextField(initialCheckIn.format(DATE_FORMAT));
        checkOutField = new JTextField(initialCheckOut.format(DATE_FORMAT));

        addField(card, "Customer Name:", customerNameField);
        addField(card, "Phone Number:", phoneField);
        addField(card, "Pet Type:", petTypeCombo);
        addField(card, "Pet Name:", petNameField);
        addField(card, "Breed:", breedField);
        addField(card, "Weight (kg):", weightField);
        addField(card, "Check-in Date (yyyy-MM-dd):", checkInField);
        addField(card, "Check-out Date (yyyy-MM-dd):", checkOutField);

        content.add(card);
        content.add(Box.createVerticalStrut(14));
        content.add(createServicesCard());

        return content;
    }

    private JPanel createServicesCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)));

        JLabel cardTitle = new JLabel("Additional Services");
        cardTitle.setFont(UIStyle.FONT_HEADING);
        cardTitle.setForeground(UIStyle.COLOR_TEXT_DARK);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        walkingCheckBox = new JCheckBox("Dog Walking (+100 THB)");
        walkingCheckBox.setFont(UIStyle.FONT_BODY);
        walkingCheckBox.setOpaque(false);
        walkingCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        groomingCheckBox = new JCheckBox("Grooming (+300 THB)");
        groomingCheckBox.setFont(UIStyle.FONT_BODY);
        groomingCheckBox.setOpaque(false);
        groomingCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(cardTitle);
        card.add(Box.createVerticalStrut(10));
        card.add(walkingCheckBox);
        card.add(Box.createVerticalStrut(6));
        card.add(groomingCheckBox);
        return card;
    }

    private void addField(JPanel parent, String labelText, Component field) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIStyle.FONT_BODY);
        label.setForeground(UIStyle.COLOR_TEXT_DARK);
        parent.add(label);
        parent.add(field);
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UIStyle.COLOR_CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(UIStyle.FONT_BUTTON);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());

        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.setFont(UIStyle.FONT_BUTTON);
        confirmButton.setBackground(UIStyle.COLOR_PRIMARY);
        confirmButton.setForeground(java.awt.Color.WHITE);
        confirmButton.setOpaque(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> submit());

        JPanel left = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(cancelButton);

        JPanel right = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(confirmButton);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    private void submit() {
        try {
            String customerName = customerNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String petType = (String) petTypeCombo.getSelectedItem();
            String petName = petNameField.getText().trim();
            String breed = breedField.getText().trim();
            double weight = Double.parseDouble(weightField.getText().trim());
            LocalDate checkIn = LocalDate.parse(checkInField.getText().trim(), DATE_FORMAT);
            LocalDate checkOut = LocalDate.parse(checkOutField.getText().trim(), DATE_FORMAT);
            boolean walking = walkingCheckBox.isSelected();
            boolean grooming = groomingCheckBox.isSelected();

            Booking booking = bookingController.createBooking(customerName, phone, petType, petName,
                    breed, weight, room, checkIn, checkOut, walking, grooming);

            JOptionPane.showMessageDialog(this,
                    "Booking successful! Booking ID: " + booking.getBookingId()
                            + "\nTotal: " + booking.getTotalPrice() + " THB");

            onBookingCreated.run();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for weight", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format (yyyy-MM-dd)", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Booking Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
