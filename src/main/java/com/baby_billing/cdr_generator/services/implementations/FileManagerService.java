package com.baby_billing.cdr_generator.services.implementations;

import java.io.File;
import com.baby_billing.cdr_generator.services.IFileManagerService;
import org.springframework.stereotype.Service;

@Service
public class FileManagerService implements IFileManagerService {

    public void checkAndCleanDataFolder(){
        File dataFolder = new File("data");
        if (dataFolder.isDirectory()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        } else {
            dataFolder.mkdir();
        }
    }
}
