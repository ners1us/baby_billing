package com.baby_billing.cdr_generator.controllers.implementations;

import com.baby_billing.cdr_generator.controllers.ICdrController;
import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.publishers.CdrToBrtRabbitMQPublisher;
import com.baby_billing.cdr_generator.services.ICdrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/v1")
public class CdrController implements ICdrController {

    private final CdrToBrtRabbitMQPublisher cdrToBrtRabbitMQPublisher;
    private final ICdrService cdrService;

    public CdrController(CdrToBrtRabbitMQPublisher cdrToBrtRabbitMQPublisher, ICdrService cdrService) {
        this.cdrToBrtRabbitMQPublisher = cdrToBrtRabbitMQPublisher;
        this.cdrService = cdrService;
    }

    @PostMapping("/generateCdr")
    public ResponseEntity<String> generateAndSaveCdr() throws ExecutionException, InterruptedException {
        Future<List<History>> futureHistoryList = cdrService.generateCdr();
        List<History> historyList = futureHistoryList.get();
        cdrService.processCdr(historyList);
        return ResponseEntity.ok("Successfully generated files");
    }

    @PostMapping("/publishCdr")
    public ResponseEntity<String> publishCdr() {
        try {
            List<History> historyList = readHistoryFromFile();
            for (History history : historyList) {
                cdrToBrtRabbitMQPublisher.sendMessage(history);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error reading files from data folder");
        }
        return ResponseEntity.ok("Messages sent to RabbitMQ...");
    }

    private List<History> readHistoryFromFile() throws IOException {
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
