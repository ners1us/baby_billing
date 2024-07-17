package com.brt.controllers;

import com.brt.entities.Client;
import com.brt.services.BrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с функциями администратора.
 */
@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final BrtDatabaseService brtDatabaseService;

    /**
     * Получает список всех клиентов.
     *
     * @return ResponseEntity со списком клиентов.
     */
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = brtDatabaseService.getAllClients();

        return ResponseEntity.ok(clients);
    }

    /**
     * Добавляет нового клиента в базу данных.
     *
     * @param client информация о новом клиенте.
     * @return ResponseEntity с сообщением об успешном добавлении клиента.
     */
    @PostMapping("/addClient")
    public ResponseEntity<String> addClient(@RequestBody Client client) {
        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Client added successfully.");
    }

    /**
     * Изменяет тариф для указанного клиента.
     *
     * @param clientId номер клиента.
     * @param tariffId идентификатор нового тарифа.
     * @return ResponseEntity с сообщением об успешном изменении тарифа.
     */
    @PutMapping("/clients/{clientId}/tariff")
    public ResponseEntity<String> changeTariff(@PathVariable String clientId, @RequestParam Integer tariffId) {
        Client client = brtDatabaseService.findClientById(clientId);

        client.setTariffId(tariffId);

        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Tariff changed to " + tariffId);
    }
}
