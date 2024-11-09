package com.brt.publishers;

import com.brt.dto.BrtHistoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Отправитель записей BRT в RabbitMQ, сервису HRS для дальнейшей обработки.
 */
@Service
@RequiredArgsConstructor
public class BrtToHrsRabbitMQPublisher {

    @Value("${rabbitmq.exchange.brt-hrs.name}")
    private String exchange;

    @Value("${rabbitmq.brt.to.hrs.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(BrtToHrsRabbitMQPublisher.class);

    /**
     * Отправляет запись BRT в RabbitMQ для сервиса HRS.
     *
     * @param brtHistoryDto запись BRT для отправки.
     */
    public void sendCallToHrs(BrtHistoryDto brtHistoryDto) {
        try {
            String json = objectMapper.writeValueAsString(brtHistoryDto);

            rabbitTemplate.convertAndSend(exchange, routingKey, json);
        } catch (JsonProcessingException ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}
