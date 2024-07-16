package com.cdr_generator.controllers;

import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.publishers.CdrToBrtRabbitMQPublisher;
import com.cdr_generator.services.CdrService;
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
@RequestMapping("/api/cdr_generator")
@AllArgsConstructor
public class CdrController {

    private final CdrToBrtRabbitMQPublisher cdrToBrtRabbitMQPublisher;

    private final CdrService cdrService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrController.class);

    @PostMapping("/generateCdr")
    public ResponseEntity<String> generateAndSaveCdr() throws IOException {
        cdrService.checkAndCleanData();

        CompletableFuture<List<CdrHistory>> cdrFuture = cdrService.generateCdr();

        List<CdrHistory> cdrHistoryList = cdrFuture.join();
        cdrService.processCdr(cdrHistoryList);

        return ResponseEntity.ok("Successfully generated files");
    }

    @PostMapping("/publishCdr")
    public ResponseEntity<String> publishCdr() {
        try {
            List<CdrHistory> cdrHistoryList = cdrService.readHistory();

            cdrHistoryList.forEach(cdrToBrtRabbitMQPublisher::sendMessage);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body("Error reading files from data folder");
        }

        return ResponseEntity.ok("Messages sent to RabbitMQ...");
    }
}
