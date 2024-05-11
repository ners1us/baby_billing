package com.baby_billing.hrs.consumers;

import com.baby_billing.hrs.services.IHrsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Потребитель RabbitMQ для обработки сообщений от BRT и передачи их в сервис HRS.
 */
@Service
@AllArgsConstructor
public class BrtToHrsRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrtToHrsRabbitMQConsumer.class);

    private final IHrsService hrsService;

    /**
     * Обрабатывает сообщения из RabbitMQ от BRT и передает их в сервис HRS.
     *
     * @param brtHistoryJson Сообщение в формате JSON, содержащее историю BRT.
     */
    @RabbitListener(queues = {"${rabbitmq.brt.to.hrs.queue.name}"})
    public void consumeMessage(String brtHistoryJson) {
        try {
            hrsService.processCallsFromBrt(brtHistoryJson);
        } catch (Exception e) {
            LOGGER.error("Error processing BrtHistory: {}", e.getMessage());
        }

        LOGGER.info(String.format("Consumed message -> %s", brtHistoryJson));
    }
}
