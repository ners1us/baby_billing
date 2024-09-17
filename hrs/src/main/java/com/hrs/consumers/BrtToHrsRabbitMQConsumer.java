package com.hrs.consumers;

import com.hrs.services.HrsProcessorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Потребитель RabbitMQ для обработки сообщений от BRT и передачи их в микросервис HRS.
 */
@Service
@AllArgsConstructor
public class BrtToHrsRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrtToHrsRabbitMQConsumer.class);

    private final HrsProcessorService hrsProcessorService;

    /**
     * Обрабатывает сообщения из RabbitMQ от BRT и передает их в микросервис HRS.
     *
     * @param brtHistoryJson сообщение в формате JSON, содержащее историю BRT.
     */
    @RabbitListener(queues = {"${rabbitmq.brt.to.hrs.queue.name}"})
    public void consumeMessage(String brtHistoryJson) {
        try {
            hrsProcessorService.processCallsFromBrt(brtHistoryJson);
        } catch (Exception e) {
            LOGGER.error("Error processing BrtHistoryDto: {}", e.getMessage());
        }

        LOGGER.info(String.format("Consumed message -> %s", brtHistoryJson));
    }
}
