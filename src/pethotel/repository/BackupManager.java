package pethotel.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class BackupManager {

    private static final String DATA_DIR = "resources/data/";
    
    private static final String[] FILES_TO_BACKUP = {
        "bookings.json",
        "customers.json",
        "rooms.json"
    };

    public static void backupData() {
        System.out.println("[BackupManager] Starting system data backup...");
        
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            System.out.println("[BackupManager] Primary data directory not found at: " + DATA_DIR + " (Skipping backup)");
            return;
        }

        for (String fileName : FILES_TO_BACKUP) {
            File sourceFile = new File(dir, fileName);
            
            if (sourceFile.exists()) {
                File backupFile = new File(dir, fileName + ".bak");
                try {
                    Files.copy(sourceFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("[BackupManager] Backup completed successfully: " + fileName + " -> " + backupFile.getName());
                } catch (IOException e) {
                    System.err.println("[BackupManager] Error occurred while backing up " + fileName + ": " + e.getMessage());
                }
            } else {
                System.out.println("[BackupManager] Original file " + fileName + " not found (No data to backup)");
            }
        }
        System.out.println("[BackupManager] Data backup process completed.");
    }
}
