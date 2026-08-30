/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



package com.mycompany.projectownerpanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BookingCalendarPanel extends JPanel {

    // ==========================================
    // Style Constants
    // ==========================================

    private static final Color COLOR_PRIMARY = new Color(79, 70, 229);
    private static final Color COLOR_BACKGROUND = new Color(245, 246, 250);
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_TEXT_DARK = new Color(31, 41, 55);
    private static final Color COLOR_TEXT_LIGHT = new Color(107, 114, 128);
    private static final Color COLOR_BORDER = new Color(224, 226, 232);

    private static final Color COLOR_AVAILABLE = new Color(52, 199, 89);
    private static final Color COLOR_BOOKED = new Color(239, 68, 68);
    private static final Color COLOR_SELECTED = new Color(250, 204, 21);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    private static final String[] WEEKDAY_LABELS =
            {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    // Number of days shown in the calendar (demo month)
    private static final int TOTAL_DAYS = 30;

    // Room selector
    private JComboBox<String> roomComboBox;

    // Calendar
    private JButton[] dayButtons;

    // Data: booked days per room, selected days per room
    private Map<String, Set<Integer>> bookedDaysByRoom;
    private Map<String, Set<Integer>> selectedDaysByRoom;

    // Footer buttons
    private JButton backButton;
    private JButton nextButton;

    public BookingCalendarPanel() {

        setLayout(new BorderLayout());

        setBackground(COLOR_BACKGROUND);

        initData();

        createHeaderPanel();
        createContentPanel();
        createFooterPanel();

        refreshCalendar();
    }

    // ==========================================
    // Init Data (demo booked days)
    // ==========================================

    private void initData() {

        bookedDaysByRoom = new HashMap<>();
        selectedDaysByRoom = new HashMap<>();

        Set<Integer> roomABooked = new HashSet<>();
        roomABooked.add(5);
        roomABooked.add(6);
        roomABooked.add(12);
        roomABooked.add(13);
        roomABooked.add(20);

        Set<Integer> roomBBooked = new HashSet<>();
        roomBBooked.add(1);
        roomBBooked.add(2);
        roomBBooked.add(15);
        roomBBooked.add(16);
        roomBBooked.add(17);

        Set<Integer> roomCBooked = new HashSet<>();
        roomCBooked.add(8);
        roomCBooked.add(9);
        roomCBooked.add(25);

        bookedDaysByRoom.put("Room A", roomABooked);
        bookedDaysByRoom.put("Room B", roomBBooked);
        bookedDaysByRoom.put("Room C", roomCBooked);

        selectedDaysByRoom.put("Room A", new HashSet<>());
        selectedDaysByRoom.put("Room B", new HashSet<>());
        selectedDaysByRoom.put("Room C", new HashSet<>());
    }

    // ==========================================
    // Header
    // ==========================================

    private void createHeaderPanel() {

        JPanel header = new JPanel();

        header.setLayout(
                new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        header.setBackground(COLOR_PRIMARY);

        header.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel titleLabel = new JLabel("Booking Calendar");

        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(
                "Step 3 of 4 - Choose a room and your dates"
        );

        subtitleLabel.setFont(FONT_SUBTITLE);
        subtitleLabel.setForeground(new Color(226, 225, 253));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(titleLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitleLabel);

        add(header, BorderLayout.NORTH);
    }

    // ==========================================
    // Content (room selector + calendar card + legend)
    // ==========================================

    private void createContentPanel() {

        JPanel content = new JPanel();

        content.setLayout(
                new BoxLayout(content, BoxLayout.Y_AXIS)
        );

        content.setBackground(COLOR_BACKGROUND);

        content.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        content.add(createRoomSelectorRow());
        content.add(Box.createVerticalStrut(15));
        content.add(createCalendarCard());
        content.add(Box.createVerticalStrut(12));
        content.add(createLegendRow());

        add(content, BorderLayout.CENTER);
    }

    private JPanel createRoomSelectorRow() {

        JPanel row = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 0)
        );

        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roomLabel = new JLabel("Room:");

        roomLabel.setFont(FONT_HEADING);
        roomLabel.setForeground(COLOR_TEXT_DARK);

        roomComboBox = new JComboBox<>(
                new String[]{"Room A", "Room B", "Room C"}
        );

        roomComboBox.setFont(FONT_BODY);
        roomComboBox.setBackground(Color.WHITE);

        roomComboBox.setBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1)
        );

        roomComboBox.addActionListener(
                e -> refreshCalendar()
        );

        row.add(roomLabel);
        row.add(roomComboBox);

        return row;
    }

    private JPanel createCalendarCard() {

        JPanel card = new JPanel(new BorderLayout(0, 10));

        card.setBackground(COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER, 1),
                        BorderFactory.createEmptyBorder(18, 20, 18, 20)
                )
        );

        // Weekday header row
        JPanel weekdayRow = new JPanel(
                new GridLayout(1, 7, 5, 5)
        );

        weekdayRow.setOpaque(false);

        for (String weekday : WEEKDAY_LABELS) {

            JLabel weekdayLabel = new JLabel(
                    weekday, SwingConstants.CENTER
            );

            weekdayLabel.setFont(FONT_HEADING);
            weekdayLabel.setForeground(COLOR_TEXT_LIGHT);

            weekdayRow.add(weekdayLabel);
        }

        // Day number grid
        JPanel dayGridPanel = new JPanel(
                new GridLayout(0, 7, 5, 5)
        );

        dayGridPanel.setOpaque(false);

        dayButtons = new JButton[TOTAL_DAYS];

        for (int i = 0; i < TOTAL_DAYS; i++) {

            final int day = i + 1;

            JButton dayButton = new JButton(
                    String.valueOf(day)
            );

            dayButton.setFont(FONT_BODY);
            dayButton.setForeground(Color.WHITE);
            dayButton.setOpaque(true);
            dayButton.setBorderPainted(false);
            dayButton.setFocusPainted(false);

            dayButton.setPreferredSize(
                    new Dimension(45, 38)
            );

            dayButton.setCursor(
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            );

            dayButton.addActionListener(
                    e -> toggleDay(day)
            );

            dayButtons[i] = dayButton;

            dayGridPanel.add(dayButton);
        }

        card.add(weekdayRow, BorderLayout.NORTH);
        card.add(dayGridPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createLegendRow() {

        JPanel legend = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 18, 0)
        );

        legend.setOpaque(false);
        legend.setAlignmentX(Component.LEFT_ALIGNMENT);

        legend.add(createLegendItem("Available", COLOR_AVAILABLE));
        legend.add(createLegendItem("Booked", COLOR_BOOKED));
        legend.add(createLegendItem("Selected", COLOR_SELECTED));

        return legend;
    }

    private JPanel createLegendItem(String label, Color color) {

        JPanel item = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 6, 0)
        );

        item.setOpaque(false);

        JLabel dot = new JLabel("\u25CF");

        dot.setForeground(color);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel text = new JLabel(label);

        text.setFont(FONT_SUBTITLE);
        text.setForeground(COLOR_TEXT_LIGHT);

        item.add(dot);
        item.add(text);

        return item;
    }

    // ==========================================
    // Footer (Back / Next)
    // ==========================================

    private void createFooterPanel() {

        JPanel footer = new JPanel(new BorderLayout());

        footer.setBackground(COLOR_CARD);

        footer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)
                )
        );

        backButton = new JButton("< Back");

        backButton.setFont(FONT_BUTTON);
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(COLOR_TEXT_DARK);
        backButton.setFocusPainted(false);

        backButton.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDER, 1),
                        BorderFactory.createEmptyBorder(9, 25, 9, 25)
                )
        );

        backButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        nextButton = new JButton("Next >");

        nextButton.setFont(FONT_BUTTON);
        nextButton.setBackground(COLOR_PRIMARY);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setOpaque(true);
        nextButton.setBorderPainted(false);

        nextButton.setBorder(
                BorderFactory.createEmptyBorder(10, 26, 10, 26)
        );

        nextButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        backButton.addActionListener(
                e -> goBack()
        );

        nextButton.addActionListener(
                e -> goToNextPage()
        );

        JPanel leftPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        leftPanel.setOpaque(false);
        leftPanel.add(backButton);

        JPanel rightPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 0, 0)
        );

        rightPanel.setOpaque(false);
        rightPanel.add(nextButton);

        footer.add(leftPanel, BorderLayout.WEST);
        footer.add(rightPanel, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);
    }

    // ==========================================
    // Back Button
    // ==========================================

    private void goBack() {

        JOptionPane.showMessageDialog(
                this,
                "Going back to the previous step."
        );
    }

    // ==========================================
    // Toggle Day Selection
    // ==========================================

    private void toggleDay(int day) {

        String room = (String) roomComboBox.getSelectedItem();

        Set<Integer> booked = bookedDaysByRoom.get(room);

        if (booked.contains(day)) {

            // Booked days cannot be selected
            return;
        }

        Set<Integer> selected = selectedDaysByRoom.get(room);

        if (selected.contains(day)) {

            selected.remove(day);

        } else {

            selected.add(day);
        }

        refreshCalendar();
    }

    // ==========================================
    // Refresh Calendar Colors
    // ==========================================

    private void refreshCalendar() {

        String room = (String) roomComboBox.getSelectedItem();

        Set<Integer> booked = bookedDaysByRoom.get(room);
        Set<Integer> selected = selectedDaysByRoom.get(room);

        for (int i = 0; i < TOTAL_DAYS; i++) {

            int day = i + 1;

            JButton dayButton = dayButtons[i];

            if (booked.contains(day)) {

                dayButton.setBackground(COLOR_BOOKED);
                dayButton.setEnabled(false);

            } else if (selected.contains(day)) {

                dayButton.setBackground(COLOR_SELECTED);
                dayButton.setEnabled(true);

            } else {

                dayButton.setBackground(COLOR_AVAILABLE);
                dayButton.setEnabled(true);
            }
        }
    }

    // ==========================================
    // Next Button
    // ==========================================

    private void goToNextPage() {

        StringBuilder summary = new StringBuilder();

        summary.append("Booking summary:\n");

        boolean hasSelection = false;

        for (String room : selectedDaysByRoom.keySet()) {

            Set<Integer> selected = selectedDaysByRoom.get(room);

            if (!selected.isEmpty()) {

                hasSelection = true;

                Set<Integer> sortedDays = new TreeSet<>(selected);

                summary.append(room)
                        .append(": ")
                        .append(sortedDays)
                        .append("\n");
            }
        }

        if (!hasSelection) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select at least one booking date."
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                summary.toString()
        );
    }

    // ==========================================
    // Main Method
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "Booking Calendar"
            );

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setSize(650, 560);

            frame.setLocationRelativeTo(null);

            frame.add(
                    new BookingCalendarPanel()
            );

            frame.setVisible(true);
        });
    }
}
