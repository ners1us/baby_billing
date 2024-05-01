package com.baby_billing.brt.publishers;

import com.baby_billing.brt.entities.BrtHistory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    public void sendCallToHrs(BrtHistory brtHistory) {
        rabbitTemplate.convertAndSend(exchange, routingKey, brtHistory);
    }
}
