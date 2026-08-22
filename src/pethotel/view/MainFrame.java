package pethotel.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import pethotel.controller.BookingController;
import pethotel.controller.CustomerController;
import pethotel.controller.RoomController;

public class MainFrame extends JFrame {

    private DashboardPanel dashboardPanel;

    public MainFrame(BookingController bookingController,
            CustomerController customerController,
            RoomController roomController) {
        setTitle("Pet Hotel System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        dashboardPanel = new DashboardPanel(this,
                customerController, roomController, bookingController);
        add(dashboardPanel, BorderLayout.CENTER);
        createMenu();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem refreshItem = new JMenuItem("Refresh Dashboard");

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        refreshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboardPanel.refreshData();
            }
        });

        fileMenu.add(exitItem);
        viewMenu.add(refreshItem);
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }
}