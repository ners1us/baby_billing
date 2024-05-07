package com.baby_billing.brt.controllers;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.publishers.BrtToHrsRabbitMQPublisher;
import com.baby_billing.brt.services.IBrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
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
}
