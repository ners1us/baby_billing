package com.baby_billing.api.controllers;

import com.baby_billing.brt.entities.Client;
import com.baby_billing.brt.services.IBrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/subscriber")
@AllArgsConstructor
public class ClientController {

    private final IBrtDatabaseService brtDatabaseService;

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam String subscriber) {
        return ResponseEntity.ok(brtDatabaseService.findClientById(subscriber).getBalance());
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositBalance(@RequestParam String subscriber, @RequestParam BigDecimal amount) {
        Client client = brtDatabaseService.findClientById(subscriber);

        client.setBalance(client.getBalance().add(amount));

        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Successfully made a deposit.");
    }
}
