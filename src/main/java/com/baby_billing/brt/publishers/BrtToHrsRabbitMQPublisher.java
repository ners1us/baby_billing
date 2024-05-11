package com.baby_billing.brt.publishers;

import com.baby_billing.brt.entities.BrtHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public void sendCallToHrs(BrtHistory brtHistory) {
        try {
            String json = objectMapper.writeValueAsString(brtHistory);

            rabbitTemplate.convertAndSend(exchange, routingKey, json);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
