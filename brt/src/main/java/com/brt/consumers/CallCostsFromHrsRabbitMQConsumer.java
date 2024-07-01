package com.brt.consumers;

import com.brt.dto.HrsCallCostDto;
import com.brt.entities.BrtHistory;
import com.brt.exceptions.NotFoundClientException;
import com.brt.services.BrtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Потребитель для получения сообщений о стоимости вызовов из RabbitMQ, отправленных из сервиса HRS.
 */
@Service
@AllArgsConstructor
public class CallCostsFromHrsRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallCostsFromHrsRabbitMQConsumer.class);

    private final BrtService brtService;

    /**
     * Обрабатывает полученное сообщение о стоимости вызовов из HRS и передает его сервису BRT для обработки.
     *
     * @param message Сообщение о стоимости вызовов из HRS в формате JSON.
     */
    @RabbitListener(queues = {"${rabbitmq.call.hrs.to.brt.queue.name}"})
    public void consumeMessage(String message) throws NotFoundClientException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            HrsCallCostDto callCost = objectMapper.readValue(message, HrsCallCostDto.class);

            BrtHistory brtHistory = new BrtHistory();
            brtHistory.setClient(callCost.getClient());
            brtHistory.setCallerId(callCost.getCallerId());
            brtHistory.setStartTime(callCost.getStartTime());
            brtHistory.setEndTime(callCost.getEndTime());
            brtService.processCostFromHrs(brtHistory, callCost.getCost());

        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing CallCost to JSON: {}", e.getMessage());
        }

        LOGGER.info(String.format("Consumed message -> %s", message));
    }
}
