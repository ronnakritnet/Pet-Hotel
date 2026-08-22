package pethotel.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import pethotel.controller.BookingController;
import pethotel.controller.CustomerController;
import pethotel.controller.RoomController;

public class DashboardPanel extends JPanel {

    private MainFrame mainFrame;
    private CustomerController customerController;
    private RoomController roomController;
    private BookingController bookingController;

    private JLabel customerCountLabel;
    private JTextArea roomArea;
    private JTextArea bookingArea;

    public DashboardPanel(MainFrame mainFrame,
            CustomerController customerController,
            RoomController roomController,
            BookingController bookingController) {

        this.mainFrame = mainFrame;
        this.customerController = customerController;
        this.roomController = roomController;
        this.bookingController = bookingController;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("PET HOTEL DASHBOARD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        roomArea = new JTextArea();
        roomArea.setEditable(false);
        roomArea.setBorder(BorderFactory.createTitledBorder("Rooms Today"));

        bookingArea = new JTextArea();
        bookingArea.setEditable(false);
        bookingArea.setBorder(BorderFactory.createTitledBorder("Bookings"));

        centerPanel.add(new JScrollPane(roomArea));
        centerPanel.add(new JScrollPane(bookingArea));
        add(centerPanel, BorderLayout.CENTER);

        customerCountLabel = new JLabel();
        add(customerCountLabel, BorderLayout.SOUTH);

        refreshData();
    }

    public void refreshData() {
        customerCountLabel.setText("Customers: "
                + customerController.getCustomers().size());

        roomArea.setText(roomController.getRoomSummary());
        bookingArea.setText(bookingController.getAllBookingsText());

        roomArea.setCaretPosition(0);
        bookingArea.setCaretPosition(0);
    }
}