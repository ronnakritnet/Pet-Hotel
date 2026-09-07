package pethotel;

import javax.swing.SwingUtilities;

import pethotel.controller.BookingController;
import pethotel.controller.CustomerController;
import pethotel.controller.RoomController;
import pethotel.repository.BackupManager;
import pethotel.repository.DataManager;
import pethotel.view.MainFrame;

public class Main {

    public static void main(String[] args) {

        // Auto-backup existing data before loading (safe to skip if no data yet)
        try {
            System.out.println("[System Info] Starting automatic data backup...");
            BackupManager.backupData();
            System.out.println("[System Info] Backup process finished successfully.");
        } catch (Exception e) {
            System.err.println("[System Warning] Data backup failed: " + e.getMessage());
        }

        // Load data files into RAM
        System.out.println("[System Info] Loading data files into RAM...");
        DataManager dataManager = DataManager.getInstance();
        try {
            dataManager.loadAllData();
            System.out.println("[System Info] All data loaded into RAM successfully.");
        } catch (Exception e) {
            System.err.println("[System Error] Database initialization failed: " + e.getMessage());
        }

        // Controllers
        System.out.println("[System Info] Initializing controllers...");
        CustomerController customerController = new CustomerController(dataManager);
        RoomController roomController = new RoomController(dataManager);
        BookingController bookingController = new BookingController(dataManager, customerController, roomController);

        // Swing UI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("[System Info] Opening Main Application Window...");

                MainFrame mainFrame = new MainFrame(bookingController, customerController, roomController);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);

                System.out.println("[System Info] Pet Hotel system is active and ready for counter-staff.");
            }
        });
    }
}
