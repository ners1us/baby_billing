package com.brt.controllers;

import com.brt.dto.BrtHistoryDto;
import com.brt.entities.BrtHistory;
import com.brt.publishers.BrtToHrsRabbitMQPublisher;
import com.brt.services.BrtDatabaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для работы с историей BRT и отправкой данных в микросервис HRS.
 */
@RestController
@RequestMapping("/api/brt")
@AllArgsConstructor
public class BrtController {

    private final BrtToHrsRabbitMQPublisher brtToHrsRabbitMQPublisher;

    private final BrtDatabaseService brtDatabaseService;

    /**
     * Отправляет конкретную историю BRT в микросервис HRS.
     *
     * @param historyId идентификатор истории BRT.
     * @return ResponseEntity с сообщением об успешной отправке истории в HRS.
     */
    @PostMapping("/sendHistoryToHrs")
    @Operation(summary = "Send specific BRT history to HRS", description = "This method allows to send data with a specific ID from the BRT service history table to the HRS service, and also receive a response back in the BRT service")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string", example = "History sent to Hrs successfully.")))
    public ResponseEntity<String> sendHistoryToHrs(@RequestParam Long historyId) {
        BrtHistory brtHistory = brtDatabaseService.findBrtHistoryById(historyId);

        BrtHistoryDto brtHistoryDto = BrtHistoryDto.fromEntity(brtHistory);

        brtToHrsRabbitMQPublisher.sendCallToHrs(brtHistoryDto);

        return ResponseEntity.ok("History sent to Hrs successfully.");
    }

    /**
     * Отправляет все истории BRT в микросервис HRS.
     *
     * @return ResponseEntity с сообщением об успешной отправке всех историй в HRS.
     */
    @PostMapping("/sendAllHistoriesToHrs")
    @Operation(summary = "Send all BRT histories to HRS", description = "This method allows to send all data from the BRT service history table to the HRS service, and also receive a response back in the BRT service")
    @ApiResponse(responseCode = "200", description = "Successful response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string", example = "All histories sent to Hrs successfully.")))
    public ResponseEntity<String> sendAllHistoriesToHrs() {
        List<BrtHistory> brtHistories = brtDatabaseService.getAllBrtHistories();

        List<BrtHistoryDto> brtHistoryDtos = BrtHistoryDto.fromEntities(brtHistories);

        brtHistoryDtos.forEach(brtToHrsRabbitMQPublisher::sendCallToHrs);

        return ResponseEntity.ok("All histories sent to Hrs successfully.");
    }
}
