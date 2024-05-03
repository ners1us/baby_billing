package com.baby_billing.hrs.consumer;

import com.baby_billing.brt.entities.BrtHistory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BrtToHrsRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrtToHrsRabbitMQConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.brt.to.hrs.queue.name}"})
    public void consumeMessage(BrtHistory brtHistory) {
        LOGGER.info(String.format("Consumed message -> %s", brtHistory.toString()));
    }
}
