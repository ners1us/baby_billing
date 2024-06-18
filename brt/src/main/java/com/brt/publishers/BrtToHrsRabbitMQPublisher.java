package com.brt.publishers;

import com.brt.entities.BrtHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
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

    @NonNull
    private RabbitTemplate rabbitTemplate;

    @NonNull
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(BrtToHrsRabbitMQPublisher.class);

    /**
     * Отправляет запись BRT в RabbitMQ для сервиса HRS.
     *
     * @param brtHistory Запись BRT для отправки.
     */
    public void sendCallToHrs(BrtHistory brtHistory) {
        try {
            String json = objectMapper.writeValueAsString(brtHistory);

            rabbitTemplate.convertAndSend(exchange, routingKey, json);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
