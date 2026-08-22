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

        BackupManager.backupData();

        DataManager dataManager = DataManager.getInstance();
        dataManager.loadAllData();

        CustomerController customerController = new CustomerController(dataManager);
        RoomController roomController = new RoomController(dataManager);
        BookingController bookingController = new BookingController(
                dataManager, customerController, roomController);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame(
                        bookingController, customerController, roomController);
                mainFrame.setVisible(true);
            }
        });
    }
}