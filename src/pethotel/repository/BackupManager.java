package repository;

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

    
    public void executeBackup() {
        System.out.println("BackupManager: เริ่มกระบวนการตรวจสอบและสำรองระบบฐานข้อมูล...");

        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            System.out.println("BackupManager: ไม่พบโฟลเดอร์เก็บข้อมูล " + DATA_DIR + " (ไม่มีฐานข้อมูลหลักที่ต้องทำการสำรองในระบบขณะนี้)");
            return;
        }

        int successCount = 0;
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
                    System.out.println("BackupManager: ทำการสำรองไฟล์สำเร็จ -> " + backupFile.getName());
                    successCount++;
                } catch (IOException e) {
                    System.err.println("BackupManager Error: เกิดข้อผิดพลาดในการคัดลอกไฟล์สำรอง " + filename + " - " + e.getMessage());
                }
            } else {
                System.out.println("BackupManager: ข้ามการสำรองไฟล์ " + filename + " (เนื่องจากยังไม่ได้สร้างระบบขึ้นครั้งแรก)");
            }
        }

        System.out.println("BackupManager: เสร็จสิ้นขั้นตอนการสำรองไฟล์ระบบเสร็จสมบูรณ์ (" + successCount + "/" + TARGET_FILES.length + " ไฟล์)");
    }
}
