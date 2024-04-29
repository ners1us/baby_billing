package com.baby_billing.brt.consumers;

import com.baby_billing.cdr_generator.entities.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CdrToBrtRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrToBrtRabbitMQConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.cdr.to.brt.queue.name}"})
    public void consumeMessage(History history) {
        LOGGER.info(String.format("Consumed message -> %s", history.toString()));
    }
}
