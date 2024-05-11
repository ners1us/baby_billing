package com.baby_billing.api.controllers;

import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.publishers.CdrToBrtRabbitMQPublisher;
import com.baby_billing.cdr_generator.services.ICdrService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/baby_billing")
@AllArgsConstructor
public class CdrController {

    private final CdrToBrtRabbitMQPublisher cdrToBrtRabbitMQPublisher;

    private final ICdrService cdrService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrController.class);

    @PostMapping("/generateCdr")
    public ResponseEntity<String> generateAndSaveCdr() throws IOException {
        cdrService.checkAndCleanData();

        CompletableFuture<List<History>> cdrFuture = cdrService.generateCdr();

        List<History> historyList = cdrFuture.join();
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
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body("Error reading files from data folder");
        }

        return ResponseEntity.ok("Messages sent to RabbitMQ...");
    }
}
