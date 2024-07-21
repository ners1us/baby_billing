package com.cdr_generator.controllers;

import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.publishers.CdrToBrtRabbitMQPublisher;
import com.cdr_generator.services.CdrService;
import com.cdr_generator.services.FileManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Контроллер для работы с CDR.
 */
@RestController
@RequestMapping("/api/cdr_generator")
@AllArgsConstructor
public class CdrController {

    private final CdrToBrtRabbitMQPublisher cdrToBrtRabbitMQPublisher;

    private final CdrService cdrService;

    private final FileManagerService fileManagerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrController.class);

    /**
     * Генерирует и сохраняет CDR.
     *
     * @return ResponseEntity с сообщением об успешной генерации файлов.
     * @throws IOException если произошла ошибка ввода-вывода.
     */
    @PostMapping("/generateCdr")
    @Operation(summary = "Creating and saving CDR files",
            description = "This method allows generating and saving CDR files")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string", example = "Successfully generated files")))
    public ResponseEntity<String> generateAndSaveCdr() throws IOException {
        fileManagerService.checkAndCleanDataFolder();

        CompletableFuture<List<CdrHistory>> cdrFuture = cdrService.generateCdr();

        List<CdrHistory> cdrHistoryList = cdrFuture.join();
        cdrService.processCdr(cdrHistoryList);

        return ResponseEntity.ok("Successfully generated files");
    }

    /**
     * Публикует CDR в RabbitMQ.
     *
     * @return ResponseEntity с информацией о статусе отправки сообщений в RabbitMQ.
     */
    @PostMapping("/publishCdr")
    @Operation(summary = "Sending CDR files",
            description = "This method allows sending generated CDR files to the BRT service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Messages sent to RabbitMQ..."))),
            @ApiResponse(responseCode = "400", description = "Error reading files from data folder",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Error reading files from data folder")))
    })
    public ResponseEntity<String> publishCdr() {
        try {
            List<CdrHistory> cdrHistoryList = fileManagerService.readHistoryFromFile();

            cdrHistoryList.forEach(cdrToBrtRabbitMQPublisher::sendMessage);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.badRequest().body("Error reading files from data folder");
        }

        return ResponseEntity.ok("Messages sent to RabbitMQ...");
    }
}
