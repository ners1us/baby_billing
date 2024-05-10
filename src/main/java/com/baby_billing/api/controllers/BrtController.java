package com.baby_billing.api.controllers;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.publishers.BrtToHrsRabbitMQPublisher;
import com.baby_billing.brt.services.IBrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/baby_billing")
@AllArgsConstructor
public class BrtController {

    private final BrtToHrsRabbitMQPublisher brtToHrsRabbitMQPublisher;

    private final IBrtDatabaseService brtDatabaseService;

    @PostMapping("/sendHistoryToHrs")
    public ResponseEntity<String> sendHistoryToHrs(@RequestParam Long historyId) {

        BrtHistory brtHistory = brtDatabaseService.findBrtHistoryById(historyId);

        brtToHrsRabbitMQPublisher.sendCallToHrs(brtHistory);

        return ResponseEntity.ok("History sent to Hrs successfully.");
    }

    @PostMapping("/sendAllHistoriesToHrs")
    public ResponseEntity<String> sendAllHistoriesToHrs() {

        List<BrtHistory> brtHistories = brtDatabaseService.getAllBrtHistories();

        for (BrtHistory brtHistory : brtHistories) {
            brtToHrsRabbitMQPublisher.sendCallToHrs(brtHistory);
        }

        return ResponseEntity.ok("All histories sent to Hrs successfully.");
    }
}