package com.brt.controllers;

import com.brt.entities.Client;
import com.brt.services.IBrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final IBrtDatabaseService brtDatabaseService;

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = brtDatabaseService.getAllClients();

        return ResponseEntity.ok(clients);
    }

    @PostMapping("/addClient")
    public ResponseEntity<String> addClient(@RequestBody Client client) {
        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Client added successfully.");
    }

    @PutMapping("/clients/{clientId}/tariff")
    public ResponseEntity<String> changeTariff(@PathVariable String clientId, @RequestParam Integer tariffId) {
        Client client = brtDatabaseService.findClientById(clientId);

        client.setTariffId(tariffId);

        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Tariff changed to " + tariffId);
    }
}
