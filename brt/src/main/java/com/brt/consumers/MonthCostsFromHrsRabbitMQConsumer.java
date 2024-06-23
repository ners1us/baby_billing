package com.brt.consumers;

import com.brt.dto.HrsMonthCostDto;
import com.brt.dto.HrsMonthCostsMessageDto;
import com.brt.entities.TariffPaymentHistory;
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
 * Потребитель для получения сообщений о стоимости месячных тарифов из RabbitMQ, отправленных из сервиса HRS.
 */
@Service
@AllArgsConstructor
public class MonthCostsFromHrsRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthCostsFromHrsRabbitMQConsumer.class);

    private final BrtService brtService;

    /**
     * Обрабатывает полученное сообщение о стоимости месячных тарифов из HRS и передает его сервису BRT для обработки.
     *
     * @param message Сообщение о стоимости месячных тарифов из HRS в формате JSON.
     */
    @RabbitListener(queues = {"${rabbitmq.month.hrs.to.brt.queue.name}"})
    public void consumeMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            HrsMonthCostsMessageDto monthCostsMessage = objectMapper.readValue(message, HrsMonthCostsMessageDto.class);

            if (monthCostsMessage.getHrsMonthCostDtos() != null) {
                for (HrsMonthCostDto monthCost : monthCostsMessage.getHrsMonthCostDtos()) {
                    TariffPaymentHistory tariffPaymentHistory = new TariffPaymentHistory();
                    tariffPaymentHistory.setClientId(monthCost.getClientId());
                    tariffPaymentHistory.setTariffId(monthCost.getTariffId());
                    tariffPaymentHistory.setCost(monthCost.getCost());
                    tariffPaymentHistory.setTime(monthCost.getEndTime());
                    brtService.processTariffChangeFromHrs(tariffPaymentHistory);
                }
            } else {
                LOGGER.error("Received MonthCostsMessage with null monthCosts field");
            }
        } catch (JsonProcessingException | NotFoundClientException e) {
            LOGGER.error("Error processing MonthCostsMessage to JSON: {}", e.getMessage());
        }

        LOGGER.info(String.format("Consumed message -> %s", message));
    }
}
