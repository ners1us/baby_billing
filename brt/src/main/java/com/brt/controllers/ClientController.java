package com.brt.controllers;

import com.brt.entities.Client;
import com.brt.services.BrtDatabaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Retrieve client's balance", description = "This method allows to get the client's balance information")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "number")))
    public ResponseEntity<BigDecimal> getBalance(@Parameter(description = "Client number", required = true)
                                                 @RequestParam String subscriber) {
        return ResponseEntity.ok(brtDatabaseService.findClientById(subscriber).getBalance());
    }

    /**
     * Пополняет баланс клиента на указанную сумму.
     *
     * @param subscriber номер клиента.
     * @param amount     сумма для пополнения баланса.
     * @return ResponseEntity с сообщением об успешном пополнении баланса.
     */
    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Deposit to client's balance", description = "This method allows to deposit a specified amount for an existing client")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string", example = "Successfully made a deposit.")))
    public ResponseEntity<String> depositBalance(@Parameter(description = "Client number", required = true)
                                                 @RequestParam String subscriber,
                                                 @Parameter(description = "Amount to deposit", required = true)
                                                 @RequestParam BigDecimal amount) {
        Client client = brtDatabaseService.findClientById(subscriber);

        client.setBalance(client.getBalance().add(amount));

        brtDatabaseService.saveClientToDatabase(client);

        return ResponseEntity.ok("Successfully made a deposit.");
    }
}
