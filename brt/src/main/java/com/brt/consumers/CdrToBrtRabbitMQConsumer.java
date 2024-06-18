package com.brt.consumers;

import com.brt.converters.CdrHistoryMessageConverter;
import com.brt.dto.CdrHistory;
import com.brt.entities.BrtHistory;
import com.brt.services.IBrtHistoryRecordManagerService;
import com.brt.services.IBrtService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

/**
 * Потребитель для получения записей CDR из RabbitMQ и передачи их сервису BRT для обработки.
 */
@Service
@RequiredArgsConstructor
public class CdrToBrtRabbitMQConsumer {

    private final IBrtService brtService;

    private final IBrtHistoryRecordManagerService brtHistoryRecordManagerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrToBrtRabbitMQConsumer.class);

    private final CdrHistoryMessageConverter cdrHistoryMessageConverter;

    /**
     * Обрабатывает полученную запись CDR из RabbitMQ, преобразует ее в объект BrtHistory и передает сервису BRT для обработки.
     *
     * @param history Запись CDR, полученная из RabbitMQ.
     */
    @RabbitListener(queues = {"${rabbitmq.cdr.to.brt.queue.name}"}, messageConverter = "cdrHistoryMessageConverter")
    public void consumeMessage(CdrHistory history) {
        LOGGER.info(String.format("Consumed message -> %s", history.toString()));

        BrtHistory brtHistory = brtHistoryRecordManagerService.convertToNewHistory(history);

        brtService.processCdr(brtHistory);
    }
}
