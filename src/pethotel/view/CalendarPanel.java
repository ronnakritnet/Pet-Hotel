package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import pethotel.controller.BookingController;
import pethotel.model.Booking;
import pethotel.model.Room;

/**
 * View-only grid showing, for each room (row) and each of the next
 * {@link #DAYS_SHOWN} days (column), whether the room is free or booked.
 */
public class CalendarPanel extends JPanel {

    private static final int DAYS_SHOWN = 7;
    private static final DateTimeFormatter HEADER_FORMAT = DateTimeFormatter.ofPattern("dd/MM");
    private static final String STATUS_AVAILABLE = "Available";

    private final BookingController bookingController;
    private final List<Room> rooms;
    private JTable table;
    private LocalDate[] dates;

    public CalendarPanel(BookingController bookingController, List<Room> rooms) {
        this.bookingController = bookingController;
        this.rooms = rooms;

        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        JLabel title = new JLabel("Room Booking Schedule (Next " + DAYS_SHOWN + " Days)");
        title.setFont(UIStyle.FONT_HEADING);
        title.setForeground(UIStyle.COLOR_TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        table = new JTable();
        table.setRowHeight(32);
        table.setFont(UIStyle.FONT_BODY);
        table.getTableHeader().setFont(UIStyle.FONT_BUTTON);
        table.setEnabled(false); // view only
        table.setCellSelectionEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyle.COLOR_BORDER, 1));
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    /** Rebuilds the grid from the current bookings - call after creating a new booking. */
    public void refresh() {
        LocalDate today = LocalDate.now();
        dates = new LocalDate[DAYS_SHOWN];
        for (int i = 0; i < DAYS_SHOWN; i++) {
            dates[i] = today.plusDays(i);
        }

        String[] columns = new String[DAYS_SHOWN + 1];
        columns[0] = "Room";
        for (int i = 0; i < DAYS_SHOWN; i++) {
            columns[i + 1] = dates[i].format(HEADER_FORMAT);
        }

        Object[][] data = new Object[rooms.size()][DAYS_SHOWN + 1];
        for (int r = 0; r < rooms.size(); r++) {
            Room room = rooms.get(r);
            data[r][0] = room.getRoomName();
            for (int c = 0; c < DAYS_SHOWN; c++) {
                Booking booking = bookingController.getBookingFor(room, dates[c]);
                data[r][c + 1] = booking == null ? STATUS_AVAILABLE : booking.getPet().getName();
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        table.setDefaultRenderer(Object.class, new StatusCellRenderer());

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        for (int c = 1; c <= DAYS_SHOWN; c++) {
            table.getColumnModel().getColumn(c).setPreferredWidth(80);
        }
    }

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);

            if (column == 0) {
                label.setFont(UIStyle.FONT_BUTTON);
                label.setBackground(UIStyle.COLOR_CARD);
                label.setForeground(UIStyle.COLOR_TEXT_DARK);
            } else if (STATUS_AVAILABLE.equals(value)) {
                label.setFont(UIStyle.FONT_BODY.deriveFont(Font.PLAIN, 12f));
                label.setBackground(UIStyle.COLOR_FREE);
                label.setForeground(UIStyle.COLOR_FREE_TEXT);
            } else {
                label.setFont(UIStyle.FONT_BODY.deriveFont(Font.BOLD, 12f));
                label.setBackground(UIStyle.COLOR_BOOKED);
                label.setForeground(UIStyle.COLOR_BOOKED_TEXT);
            }
            return label;
        }
    }
}
