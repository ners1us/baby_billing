package com.baby_billing.cdr_generator.publishers;

import com.baby_billing.cdr_generator.entities.History;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CdrToBrtRabbitMQPublisher {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.cdr.to.brt.key}")
    private String routingCdrKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrToBrtRabbitMQPublisher.class);

    @NonNull
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(History history) {
        LOGGER.info(String.format("Message sent -> %s", history.toString()));
        rabbitTemplate.convertAndSend(exchange, routingCdrKey, history);
    }
}
