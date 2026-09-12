package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import pethotel.controller.BookingController;
import pethotel.controller.RoomController;
import pethotel.model.Customer;
import pethotel.model.Pet;
import pethotel.model.Room;

/**
 * Step 2 of the booking flow. Shows one column per night of the stay and
 * one row per eligible room (matching the pet's type and weight), so a
 * different room can be chosen for each night if needed. Also collects
 * add-on services (walking / grooming) and shows a running total.
 */
public class RoomServicePanel extends JPanel {

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter COLUMN_FORMAT = DateTimeFormatter.ofPattern("EEE d MMM");

    private final BookingController bookingController;
    private final RoomController roomController;
    private final Customer customer;
    private final Pet pet;
    private final Runnable onBack;
    private final Consumer<BookingDraft> onNext;

    private JTextField checkInField;
    private JSpinner nightsSpinner;
    private JPanel gridPanel;
    private JCheckBox walkingCheckBox;
    private JCheckBox groomingCheckBox;
    private JLabel totalLabel;

    private LocalDate[] nightDates;
    /** One selected room per night; absent entry means nothing chosen yet for that night. */
    private final Map<LocalDate, Room> selectedRooms = new LinkedHashMap<>();

    public RoomServicePanel(BookingController bookingController, RoomController roomController,
            Customer customer, Pet pet, Runnable onBack, Consumer<BookingDraft> onNext) {
        this.bookingController = bookingController;
        this.roomController = roomController;
        this.customer = customer;
        this.pet = pet;
        this.onBack = onBack;
        this.onNext = onNext;

        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        rebuildGrid();
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

        JLabel title = new JLabel("Room & Services - " + pet.getName());
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(java.awt.Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Pick any open room for each night - like choosing a seat, "
                + "booked rooms can't be selected");
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

    private JPanel createBody() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        content.add(createStayControls());
        content.add(Box.createVerticalStrut(14));

        gridPanel = new JPanel();
        gridPanel.setBackground(UIStyle.COLOR_CARD);
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        JScrollPane gridScroll = new JScrollPane(gridPanel);
        gridScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridScroll.setBorder(null);
        gridScroll.setPreferredSize(new Dimension(700, 260));

        content.add(gridScroll);
        content.add(Box.createVerticalStrut(10));
        content.add(createLegendRow());
        content.add(Box.createVerticalStrut(14));
        content.add(createServicesCard());
        content.add(Box.createVerticalStrut(10));

        totalLabel = new JLabel("Estimated Total: 0 THB");
        totalLabel.setFont(UIStyle.FONT_HEADING);
        totalLabel.setForeground(UIStyle.COLOR_TEXT_DARK);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(totalLabel);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UIStyle.COLOR_BACKGROUND);
        wrapper.add(content, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel createStayControls() {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel checkInLabel = new JLabel("Check-in (yyyy-MM-dd):");
        checkInLabel.setFont(UIStyle.FONT_BODY);
        checkInField = new JTextField(LocalDate.now().format(INPUT_FORMAT), 10);
        checkInField.setFont(UIStyle.FONT_BODY);

        JLabel nightsLabel = new JLabel("Nights:");
        nightsLabel.setFont(UIStyle.FONT_BODY);
        nightsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 14, 1));

        JButton updateButton = new JButton("Update");
        updateButton.setFont(UIStyle.FONT_BUTTON);
        updateButton.addActionListener(e -> rebuildGrid());

        card.add(checkInLabel);
        card.add(checkInField);
        card.add(nightsLabel);
        card.add(nightsSpinner);
        card.add(updateButton);
        return card;
    }

    private JPanel createLegendRow() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        legend.setOpaque(false);
        legend.setAlignmentX(Component.LEFT_ALIGNMENT);
        legend.add(legendItem("Available", UIStyle.COLOR_CAL_AVAILABLE));
        legend.add(legendItem("Booked", UIStyle.COLOR_CAL_BOOKED));
        legend.add(legendItem("Selected", UIStyle.COLOR_CAL_SELECTED));
        return legend;
    }

    private JPanel legendItem(String label, java.awt.Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);
        JLabel dot = new JLabel("\u25CF");
        dot.setForeground(color);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel text = new JLabel(label);
        text.setFont(UIStyle.FONT_SUBTITLE);
        item.add(dot);
        item.add(text);
        return item;
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
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        walkingCheckBox = new JCheckBox("Dog Walking (+" + (int) BookingController.WALKING_PRICE + " THB)");
        walkingCheckBox.setFont(UIStyle.FONT_BODY);
        walkingCheckBox.setOpaque(false);
        walkingCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        walkingCheckBox.addActionListener(e -> updateTotal());

        groomingCheckBox = new JCheckBox("Grooming (+" + (int) BookingController.GROOMING_PRICE + " THB)");
        groomingCheckBox.setFont(UIStyle.FONT_BODY);
        groomingCheckBox.setOpaque(false);
        groomingCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        groomingCheckBox.addActionListener(e -> updateTotal());

        card.add(cardTitle);
        card.add(Box.createVerticalStrut(10));
        card.add(walkingCheckBox);
        card.add(Box.createVerticalStrut(6));
        card.add(groomingCheckBox);
        return card;
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
        backButton.addActionListener(e -> onBack.run());

        JButton nextButton = new JButton("Next >");
        nextButton.setFont(UIStyle.FONT_BUTTON);
        nextButton.setBackground(UIStyle.COLOR_PRIMARY);
        nextButton.setForeground(java.awt.Color.WHITE);
        nextButton.setOpaque(true);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.addActionListener(e -> goNext());

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

    // ===================== Grid building =====================

    private void rebuildGrid() {
        LocalDate checkIn;
        try {
            checkIn = LocalDate.parse(checkInField.getText().trim(), INPUT_FORMAT);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid check-in date, expected yyyy-MM-dd.");
            return;
        }
        int nights = (Integer) nightsSpinner.getValue();

        nightDates = new LocalDate[nights];
        for (int i = 0; i < nights; i++) {
            nightDates[i] = checkIn.plusDays(i);
        }
        selectedRooms.clear();

        // Show every room - no type/weight filtering. Pick a room for each night the
        // way you'd pick a seat: any room not already booked that night is selectable.
        List<Room> allRooms = roomController.getAllRooms();

        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(allRooms.size() + 1, nights + 1, 4, 4));

        gridPanel.add(new JLabel(""));
        for (LocalDate date : nightDates) {
            JLabel header = new JLabel(date.format(COLUMN_FORMAT), SwingConstants.CENTER);
            header.setFont(UIStyle.FONT_BUTTON);
            gridPanel.add(header);
        }

        if (allRooms.isEmpty()) {
            JLabel warning = new JLabel("No rooms are configured yet.");
            warning.setForeground(UIStyle.COLOR_BOOKED_TEXT);
            gridPanel.add(warning);
        }

        // One ButtonGroup per night column so only one room can be picked for that night.
        ButtonGroup[] columnGroups = new ButtonGroup[nights];
        for (int c = 0; c < nights; c++) {
            columnGroups[c] = new ButtonGroup();
        }

        for (Room room : allRooms) {
            JLabel roomLabel = new JLabel(room.getRoomName());
            roomLabel.setFont(UIStyle.FONT_BODY);
            gridPanel.add(roomLabel);

            for (int c = 0; c < nights; c++) {
                LocalDate date = nightDates[c];
                boolean booked = !roomController.isRoomAvailable(room, date);

                JToggleButton button = new JToggleButton(booked ? "Booked" : "Available");
                button.setFont(UIStyle.FONT_SMALL);
                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setPreferredSize(new Dimension(90, 34));

                if (booked) {
                    button.setEnabled(false);
                    button.setBackground(UIStyle.COLOR_CAL_BOOKED);
                    button.setForeground(java.awt.Color.WHITE);
                } else {
                    button.setBackground(UIStyle.COLOR_CAL_AVAILABLE);
                    button.setForeground(java.awt.Color.WHITE);
                    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    columnGroups[c].add(button);
                    button.addActionListener(e -> onRoomPicked(date, room, button));
                }
                gridPanel.add(button);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
        updateTotal();
    }

    private void onRoomPicked(LocalDate date, Room room, JToggleButton button) {
        selectedRooms.put(date, room);
        // Re-paint every button in this column so the newly picked one turns yellow
        // and any previously picked one in the same column goes back to green.
        for (Component comp : gridPanel.getComponents()) {
            if (comp instanceof JToggleButton) {
                JToggleButton b = (JToggleButton) comp;
                if (b.isEnabled()) {
                    b.setBackground(b.isSelected() ? UIStyle.COLOR_CAL_SELECTED : UIStyle.COLOR_CAL_AVAILABLE);
                    b.setForeground(b.isSelected() ? UIStyle.COLOR_TEXT_DARK : java.awt.Color.WHITE);
                    b.setText(b.isSelected() ? "Selected" : "Available");
                }
            }
        }
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (Room room : selectedRooms.values()) {
            total += room.getPricePerNight();
        }
        if (walkingCheckBox != null && walkingCheckBox.isSelected()) {
            total += BookingController.WALKING_PRICE;
        }
        if (groomingCheckBox != null && groomingCheckBox.isSelected()) {
            total += BookingController.GROOMING_PRICE;
        }
        if (totalLabel != null) {
            totalLabel.setText(String.format("Estimated Total: %.0f THB", total));
        }
    }

    private void goNext() {
        if (nightDates == null || selectedRooms.size() < nightDates.length) {
            JOptionPane.showMessageDialog(this, "Please choose a room for every night of the stay.");
            return;
        }

        BookingDraft draft = new BookingDraft();
        draft.customer = customer;
        draft.pet = pet;
        draft.roomPerNight = new LinkedHashMap<>(selectedRooms);
        draft.checkIn = nightDates[0];
        draft.checkOut = nightDates[nightDates.length - 1].plusDays(1);
        draft.walking = walkingCheckBox.isSelected();
        draft.grooming = groomingCheckBox.isSelected();

        double total = 0;
        for (Room room : draft.roomPerNight.values()) {
            total += room.getPricePerNight();
        }
        if (draft.walking) {
            total += BookingController.WALKING_PRICE;
        }
        if (draft.grooming) {
            total += BookingController.GROOMING_PRICE;
        }
        draft.estimatedTotal = total;

        onNext.accept(draft);
    }
}
