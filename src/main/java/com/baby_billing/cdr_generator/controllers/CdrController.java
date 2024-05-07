package com.baby_billing.cdr_generator.controllers;

import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.publishers.CdrToBrtRabbitMQPublisher;
import com.baby_billing.cdr_generator.services.ICdrService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CdrController {

    private final CdrToBrtRabbitMQPublisher cdrToBrtRabbitMQPublisher;

    private final ICdrService cdrService;

    @PostMapping("/generateCdr")
    public ResponseEntity<String> generateAndSaveCdr() throws ExecutionException, InterruptedException, IOException {
        cdrService.checkAndCleanData();
        Future<List<History>> futureHistoryList = cdrService.generateCdr();
        List<History> historyList = futureHistoryList.get();

        cdrService.processCdr(historyList);

        return ResponseEntity.ok("Successfully generated files");
    }

    @PostMapping("/publishCdr")
    public ResponseEntity<String> publishCdr() {
        try {
            List<History> historyList = cdrService.readHistory();
            for (History history : historyList) {
                cdrToBrtRabbitMQPublisher.sendMessage(history);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error reading files from data folder");
        }

        return ResponseEntity.ok("Messages sent to RabbitMQ...");
    }
}
