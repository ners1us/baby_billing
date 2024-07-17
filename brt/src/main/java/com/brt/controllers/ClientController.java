package com.brt.controllers;

import com.brt.entities.Client;
import com.brt.services.BrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Контроллер для работы с функциями клиента.
 */
@RestController
@RequestMapping("/api/subscriber")
@AllArgsConstructor
public class ClientController {

    private final BrtDatabaseService brtDatabaseService;

    /**
     * Получает баланс клиента по номеру клиента.
     *
     * @param subscriber номер клиента.
     * @return ResponseEntity с текущим балансом клиента.
     */
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam String subscriber) {
        return ResponseEntity.ok(brtDatabaseService.findClientById(subscriber).getBalance());
    }

    /**
     * Пополняет баланс клиента на указанную сумму.
     *
     * @param subscriber номер клиента.
     * @param amount сумма для пополнения баланса.
     * @return ResponseEntity с сообщением об успешном пополнении баланса.
     */
    @PostMapping("/deposit")
    public ResponseEntity<String> depositBalance(@RequestParam String subscriber, @RequestParam BigDecimal amount) {
        Client client = brtDatabaseService.findClientById(subscriber);

        client.setBalance(client.getBalance().add(amount));

        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Successfully made a deposit.");
    }
}
