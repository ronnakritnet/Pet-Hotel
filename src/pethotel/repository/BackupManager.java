package pethotel.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BackupManager {

    private Path dataFolder;

    public BackupManager() {
        this(Paths.get("src", "resources", "data"));
    }

    public BackupManager(Path dataFolder) {
        this.dataFolder = dataFolder;
    }

    public static void backupData() {
        BackupManager backupManager = new BackupManager();
        backupManager.backup();
    }

    public void backup() {
        String[] files = {
            "customers.json",
            "rooms.json",
            "bookings.json"
        };

        try {
            Files.createDirectories(dataFolder);

            for (String fileName : files) {
                Path source = dataFolder.resolve(fileName);
                Path backup = dataFolder.resolve(fileName + ".bak");

                if (Files.exists(source)) {
                    Files.copy(source, backup,
                            StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot backup JSON files: "
                    + e.getMessage());
        }
    }
}