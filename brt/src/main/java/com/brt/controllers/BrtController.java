package com.brt.controllers;

import com.brt.entities.BrtHistory;
import com.brt.publishers.BrtToHrsRabbitMQPublisher;
import com.brt.services.BrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/brt")
@AllArgsConstructor
public class BrtController {

    private final BrtToHrsRabbitMQPublisher brtToHrsRabbitMQPublisher;

    private final BrtDatabaseService brtDatabaseService;

    @PostMapping("/sendHistoryToHrs")
    public ResponseEntity<String> sendHistoryToHrs(@RequestParam Long historyId) {

        BrtHistory brtHistory = brtDatabaseService.findBrtHistoryById(historyId);

        brtToHrsRabbitMQPublisher.sendCallToHrs(brtHistory);

        return ResponseEntity.ok("History sent to Hrs successfully.");
    }

    @PostMapping("/sendAllHistoriesToHrs")
    public ResponseEntity<String> sendAllHistoriesToHrs() {

        List<BrtHistory> brtHistories = brtDatabaseService.getAllBrtHistories();

        brtHistories.forEach(brtToHrsRabbitMQPublisher::sendCallToHrs);

        return ResponseEntity.ok("All histories sent to Hrs successfully.");
    }
}
