package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import pethotel.controller.BookingController;
import pethotel.model.Booking;
import pethotel.model.Room;

/**
 * Booking history screen with search, status filter and a read-only detail view.
 * Search matches booking ID, customer, phone, pet, room and dates.
 */
public class BookingHistoryPanel extends JPanel {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DETAIL_DATE_FORMAT = DateTimeFormatter.ofPattern("EEE d MMM yyyy");

    private final BookingController bookingController;
    private final Runnable onBack;

    private final JTextField searchField = new JTextField();
    private final JComboBox<String> statusFilter = new JComboBox<>();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel resultLabel = new JLabel();
    private final JButton detailButton = new JButton("View Details");
    private final List<Booking> displayedBookings = new ArrayList<>();

    public BookingHistoryPanel(BookingController bookingController, Runnable onBack) {
        this.bookingController = bookingController;
        this.onBack = onBack;

        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);

        tableModel = new DefaultTableModel(
                new Object[] {"Booking ID", "Customer", "Phone", "Pet", "Check-in", "Check-out", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        configureTable();

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        installListeners();
        refresh();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setBackground(UIStyle.COLOR_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(16, 22, 16, 22)));

        JButton backButton = new JButton("< Back");
        backButton.setFont(UIStyle.FONT_BUTTON);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> onBack.run());

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Booking History");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(UIStyle.COLOR_TEXT_DARK);

        JLabel subtitle = new JLabel("Search, filter and view saved booking details");
        subtitle.setFont(UIStyle.FONT_SUBTITLE);
        subtitle.setForeground(UIStyle.COLOR_TEXT_LIGHT);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitle);

        header.add(backButton, BorderLayout.WEST);
        header.add(titlePanel, BorderLayout.CENTER);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        content.add(createSearchAndFilterBar(), BorderLayout.NORTH);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1));
        tableScroll.getVerticalScrollBar().setUnitIncrement(20);
        tableScroll.getHorizontalScrollBar().setUnitIncrement(20);
        content.add(tableScroll, BorderLayout.CENTER);

        return content;
    }

    private JPanel createSearchAndFilterBar() {
        JPanel card = new JPanel(new BorderLayout(14, 0));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(UIStyle.FONT_BUTTON);
        searchField.setFont(UIStyle.FONT_BODY);
        searchField.setToolTipText("Booking ID, customer, phone, pet, room or date");
        searchField.setPreferredSize(new Dimension(430, 36));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterPanel.setOpaque(false);
        JLabel filterLabel = new JLabel("Status:");
        filterLabel.setFont(UIStyle.FONT_BUTTON);
        statusFilter.setModel(new DefaultComboBoxModel<>(new String[] {
            "ALL", "UPCOMING", "STAYING NOW", "COMPLETED"
        }));
        statusFilter.setFont(UIStyle.FONT_BODY);
        statusFilter.setPreferredSize(new Dimension(165, 36));
        filterPanel.add(filterLabel);
        filterPanel.add(statusFilter);

        card.add(searchPanel, BorderLayout.CENTER);
        card.add(filterPanel, BorderLayout.EAST);
        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UIStyle.COLOR_CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(13, 22, 13, 22)));

        resultLabel.setFont(UIStyle.FONT_BODY);
        resultLabel.setForeground(UIStyle.COLOR_TEXT_LIGHT);

        detailButton.setFont(UIStyle.FONT_BUTTON);
        detailButton.setBackground(UIStyle.COLOR_PRIMARY);
        detailButton.setForeground(Color.WHITE);
        detailButton.setOpaque(true);
        detailButton.setBorderPainted(false);
        detailButton.setFocusPainted(false);
        detailButton.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        detailButton.setEnabled(false);
        detailButton.addActionListener(e -> showSelectedBookingDetails());

        footer.add(resultLabel, BorderLayout.WEST);
        footer.add(detailButton, BorderLayout.EAST);
        return footer;
    }

    private void configureTable() {
        table.setFont(UIStyle.FONT_BODY);
        table.setRowHeight(38);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(UIStyle.COLOR_BORDER);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.getTableHeader().setFont(UIStyle.FONT_BUTTON);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setAutoCreateRowSorter(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(175);
        table.getColumnModel().getColumn(2).setPreferredWidth(125);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(115);
        table.getColumnModel().getColumn(5).setPreferredWidth(115);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);
        table.getColumnModel().getColumn(7).setPreferredWidth(135);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int c : new int[] {0, 2, 4, 5, 6}) {
            table.getColumnModel().getColumn(c).setCellRenderer(centerRenderer);
        }
        table.getColumnModel().getColumn(7).setCellRenderer(new StatusRenderer());
    }

    private void installListeners() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilters();
            }
        });

        statusFilter.addActionListener(e -> applyFilters());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                detailButton.setEnabled(table.getSelectedRow() >= 0);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    showSelectedBookingDetails();
                }
            }
        });
    }

    /** Reloads booking data and re-applies the current search/filter. */
    public void refresh() {
        applyFilters();
    }

    private void applyFilters() {
        String query = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String selectedStatus = String.valueOf(statusFilter.getSelectedItem());

        displayedBookings.clear();
        tableModel.setRowCount(0);

        for (Booking booking : bookingController.getBookings()) {
            String status = bookingController.getStatus(booking);

            if (!"ALL".equals(selectedStatus) && !selectedStatus.equals(status)) {
                continue;
            }
            if (!matchesSearch(booking, status, query)) {
                continue;
            }

            displayedBookings.add(booking);
            tableModel.addRow(new Object[] {
                booking.getBookingId(),
                booking.getCustomer().getName(),
                booking.getCustomer().getPhoneNumber(),
                booking.getPet().getName() + " (" + booking.getPet().getPetType() + ")",
                booking.getCheckInDate().format(DATE_FORMAT),
                booking.getCheckOutDate().format(DATE_FORMAT),
                String.format("%.0f THB", booking.getTotalPrice()),
                status
            });
        }

        table.clearSelection();
        detailButton.setEnabled(false);
        int total = bookingController.getBookings().size();
        resultLabel.setText("Showing " + displayedBookings.size() + " of " + total + " booking(s)");
    }

    private boolean matchesSearch(Booking booking, String status, String query) {
        if (query.isEmpty()) {
            return true;
        }

        StringBuilder searchable = new StringBuilder();
        appendSearch(searchable, booking.getBookingId());
        appendSearch(searchable, booking.getCustomer().getName());
        appendSearch(searchable, booking.getCustomer().getPhoneNumber());
        appendSearch(searchable, booking.getPet().getName());
        appendSearch(searchable, booking.getPet().getPetType());
        appendSearch(searchable, booking.getPet().getBreed());
        appendSearch(searchable, booking.getCheckInDate().toString());
        appendSearch(searchable, booking.getCheckOutDate().toString());
        appendSearch(searchable, booking.getCheckInDate().format(DATE_FORMAT));
        appendSearch(searchable, booking.getCheckOutDate().format(DATE_FORMAT));
        appendSearch(searchable, status);

        if (booking.getRoomAllocations() != null) {
            for (Room room : booking.getRoomAllocations().values()) {
                if (room != null) {
                    appendSearch(searchable, room.getRoomId());
                    appendSearch(searchable, room.getRoomName());
                    appendSearch(searchable, room.getRoomType());
                }
            }
        }

        return searchable.toString().toLowerCase(Locale.ROOT).contains(query);
    }

    private void appendSearch(StringBuilder builder, Object value) {
        if (value != null) {
            builder.append(' ').append(value);
        }
    }

    private void showSelectedBookingDetails() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        if (modelRow < 0 || modelRow >= displayedBookings.size()) {
            return;
        }

        Booking booking = displayedBookings.get(modelRow);
        showBookingDialog(booking);
    }

    private void showBookingDialog(Booking booking) {
        JDialog dialog = new JDialog(
                javax.swing.SwingUtilities.getWindowAncestor(this),
                "Booking Details - " + booking.getBookingId(),
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(UIStyle.COLOR_BACKGROUND);

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(UIStyle.COLOR_PRIMARY);
        titleBar.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel title = new JLabel("Booking " + booking.getBookingId());
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(Color.WHITE);
        JLabel status = new JLabel(bookingController.getStatus(booking));
        status.setFont(UIStyle.FONT_BUTTON);
        status.setForeground(Color.WHITE);
        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(status, BorderLayout.EAST);
        dialog.add(titleBar, BorderLayout.NORTH);

        JPanel detailContent = createDetailsContent(booking);
        JScrollPane scroll = new JScrollPane(detailContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        dialog.add(scroll, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(UIStyle.FONT_BUTTON);
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(UIStyle.COLOR_CARD);
        bottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyle.COLOR_BORDER),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        bottom.add(closeButton);
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setSize(760, 690);
        dialog.setMinimumSize(new Dimension(650, 560));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createDetailsContent(Booking booking) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIStyle.COLOR_BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        content.add(detailCard("Customer Information",
                "Name: " + booking.getCustomer().getName(),
                "Phone: " + booking.getCustomer().getPhoneNumber()));
        content.add(Box.createVerticalStrut(12));

        content.add(detailCard("Pet Information",
                "Type: " + booking.getPet().getPetType(),
                "Name: " + booking.getPet().getName(),
                "Breed: " + booking.getPet().getBreed(),
                "Weight: " + booking.getPet().getWeight() + " kg"));
        content.add(Box.createVerticalStrut(12));

        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        content.add(detailCard("Stay Information",
                "Check-in: " + booking.getCheckInDate().format(DETAIL_DATE_FORMAT),
                "Check-out: " + booking.getCheckOutDate().format(DETAIL_DATE_FORMAT),
                "Nights: " + nights,
                "Status: " + bookingController.getStatus(booking)));
        content.add(Box.createVerticalStrut(12));

        content.add(createRoomDetailsCard(booking));
        content.add(Box.createVerticalStrut(12));

        boolean dog = "DOG".equalsIgnoreCase(booking.getPet().getPetType());
        if (dog) {
            content.add(detailCard("Add-on Services",
                    "Dog Walking: " + (booking.isExtraWalking() ? "Yes (+" + (int) Booking.WALKING_PRICE + " THB)" : "No"),
                    "Grooming: " + (booking.isExtraGrooming() ? "Yes (+" + (int) Booking.GROOMING_PRICE + " THB)" : "No")));
        } else {
            content.add(detailCard("Add-on Services",
                    "Grooming: " + (booking.isExtraGrooming() ? "Yes (+" + (int) Booking.GROOMING_PRICE + " THB)" : "No")));
        }
        content.add(Box.createVerticalStrut(12));

        double roomTotal = 0.0;
        if (booking.getRoomAllocations() != null) {
            for (Room room : booking.getRoomAllocations().values()) {
                if (room != null) {
                    roomTotal += room.getPricePerNight();
                }
            }
        }
        double addOnTotal = Math.max(0.0, booking.getTotalPrice() - roomTotal);
        content.add(detailCard("Price Summary",
                String.format("Room charges: %.0f THB", roomTotal),
                String.format("Add-on services: %.0f THB", addOnTotal),
                String.format("Total: %.0f THB", booking.getTotalPrice())));

        return content;
    }

    private JPanel createRoomDetailsCard(Booking booking) {
        JPanel card = createCardShell("Room Detail (per night)");
        Map<LocalDate, Room> sorted = new TreeMap<>();
        if (booking.getRoomAllocations() != null) {
            sorted.putAll(booking.getRoomAllocations());
        }

        if (sorted.isEmpty()) {
            JLabel none = new JLabel("No room allocation data");
            none.setFont(UIStyle.FONT_BODY);
            none.setForeground(UIStyle.COLOR_TEXT_LIGHT);
            card.add(none);
            return card;
        }

        for (Map.Entry<LocalDate, Room> entry : sorted.entrySet()) {
            Room room = entry.getValue();
            String lineText = entry.getKey().format(DETAIL_DATE_FORMAT)
                    + "  -  " + room.getRoomName()
                    + "  (" + (int) room.getPricePerNight() + " THB)";
            JLabel line = new JLabel(lineText);
            line.setFont(UIStyle.FONT_BODY);
            line.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(line);
            card.add(Box.createVerticalStrut(3));
        }
        return card;
    }

    private JPanel detailCard(String title, String... lines) {
        JPanel card = createCardShell(title);
        for (String line : lines) {
            JLabel label = new JLabel(line);
            label.setFont(UIStyle.FONT_BODY);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(label);
            card.add(Box.createVerticalStrut(3));
        }
        return card;
    }

    private JPanel createCardShell(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIStyle.COLOR_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(13, 16, 13, 16)));

        JLabel heading = new JLabel(title);
        heading.setFont(UIStyle.FONT_HEADING);
        heading.setForeground(UIStyle.COLOR_TEXT_DARK);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(heading);
        card.add(Box.createVerticalStrut(8));
        return card;
    }

    private static class StatusRenderer extends DefaultTableCellRenderer {
        StatusRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                String status = String.valueOf(value);
                if ("COMPLETED".equals(status)) {
                    component.setBackground(new Color(243, 244, 246));
                    component.setForeground(new Color(75, 85, 99));
                } else if ("STAYING NOW".equals(status)) {
                    component.setBackground(UIStyle.COLOR_FREE);
                    component.setForeground(UIStyle.COLOR_FREE_TEXT);
                } else {
                    component.setBackground(new Color(254, 249, 195));
                    component.setForeground(UIStyle.COLOR_YELLOW_TEXT);
                }
            }
            return component;
        }
    }
}
