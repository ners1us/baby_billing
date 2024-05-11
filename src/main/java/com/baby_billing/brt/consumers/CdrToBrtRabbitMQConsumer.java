package com.baby_billing.brt.consumers;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.services.IBrtService;
import com.baby_billing.brt.services.IBrtHistoryRecordManagerService;
import com.baby_billing.cdr_generator.entities.History;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Потребитель для получения записей CDR из RabbitMQ и передачи их сервису BRT для обработки.
 */
@Service
@AllArgsConstructor
public class CdrToBrtRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrToBrtRabbitMQConsumer.class);

    private final IBrtService brtService;

    private final IBrtHistoryRecordManagerService brtHistoryRecordManagerService;

    /**
     * Обрабатывает полученную запись CDR из RabbitMQ, преобразует ее в объект BrtHistory и передает сервису BRT для обработки.
     *
     * @param history Запись CDR, полученная из RabbitMQ.
     */
    @RabbitListener(queues = {"${rabbitmq.cdr.to.brt.queue.name}"})
    public void consumeMessage(History history) {
        LOGGER.info(String.format("Consumed message -> %s", history.toString()));

        BrtHistory brtHistory = brtHistoryRecordManagerService.convertToNewHistory(history);

        brtService.processCdr(brtHistory);
    }
}
