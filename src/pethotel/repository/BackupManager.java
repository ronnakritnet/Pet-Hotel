package pethotel.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BackupManager {

    private static final String DATA_DIR = "resources/data/";
    private static final String[] TARGET_FILES = {
        "bookings.json",
        "customers.json",
        "rooms.json"
    };

    public static void backupData() {
        BackupManager backupManager = new BackupManager();
        backupManager.executeBackup();
    }

    public void executeBackup() {
        File dataDir = new File(DATA_DIR);

        if (!dataDir.exists()) {
            dataDir.mkdirs();
            return;
        }

        for (String filename : TARGET_FILES) {
            File sourceFile = new File(dataDir, filename);

            if (sourceFile.exists()) {
                File backupFile = new File(dataDir, filename + ".bak");

                try {
                    Files.copy(
                            sourceFile.toPath(),
                            backupFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                } catch (IOException e) {
                    System.err.println("Backup Error: " + e.getMessage());
                }
            }
        }
    }
}