package com.baby_billing.cdr_generator.services.implementations;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.services.IFileManagerService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public List<History> readHistoryFromFile() throws IOException {
        List<History> historyList = new ArrayList<>();
        File dataFolder = new File("data");
        if (dataFolder.isDirectory()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                String[] parts = line.split(",");
                                if (parts.length == 5) {
                                    History history = new History();
                                    history.setType(parts[0]);
                                    history.setClient(parseClient(parts[1]));
                                    history.setCaller(parseClient(parts[2]));
                                    history.setStartTime(Long.parseLong(parts[3]));
                                    history.setEndTime(Long.parseLong(parts[4]));
                                    historyList.add(history);
                                }
                            }
                        }
                    }
                }
            }
        }
        return historyList;
    }

    private Client parseClient(String phone) {
        Client client = new Client();
        client.setPhoneNumber(phone);
        return client;
    }
}
