package com.brt.controllers;

import com.brt.entities.Client;
import com.brt.exceptions.NotFoundClientException;
import com.brt.services.BrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/subscriber")
@AllArgsConstructor
public class ClientController {

    private final BrtDatabaseService brtDatabaseService;

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam String subscriber) throws NotFoundClientException {
        return ResponseEntity.ok(brtDatabaseService.findClientById(subscriber).getBalance());
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositBalance(@RequestParam String subscriber, @RequestParam BigDecimal amount) throws NotFoundClientException {
        Client client = brtDatabaseService.findClientById(subscriber);

        client.setBalance(client.getBalance().add(amount));

        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Successfully made a deposit.");
    }
}
