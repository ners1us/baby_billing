package com.brt.controllers;

import com.brt.entities.Client;
import com.brt.services.BrtDatabaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Retrieve all clients",
            description = "This method retrieves data of all clients")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Client.class)))

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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Add a new client",
            description = "This method adds a new client to the BRT service database, in the 'clients' table")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string", example = "Client added successfully.")))

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
    @PutMapping("/clients/changeTariff")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Change client's tariff",
            description = "This method changes the tariff for an existing client")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string", example = "Tariff changed to 11")))

    public ResponseEntity<String> changeTariff(@Parameter(description = "Client ID", required = true)
                                               @RequestParam String clientId,
                                               @Parameter(description = "New tariff ID", required = true)
                                               @RequestParam Integer tariffId) {
        Client client = brtDatabaseService.findClientById(clientId);

        client.setTariffId(tariffId);

        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Tariff changed to " + tariffId);
    }
}
