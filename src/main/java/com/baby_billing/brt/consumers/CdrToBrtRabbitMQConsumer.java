package com.baby_billing.brt.consumers;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.services.IBrtService;
import com.baby_billing.brt.services.IHistoryRecordManagerService;
import com.baby_billing.cdr_generator.entities.History;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CdrToBrtRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrToBrtRabbitMQConsumer.class);

    private final IBrtService brtService;

    private final IHistoryRecordManagerService historyRecordManagerService;

    @RabbitListener(queues = {"${rabbitmq.cdr.to.brt.queue.name}"})
    public void consumeMessage(History history) {
        LOGGER.info(String.format("Consumed message -> %s", history.toString()));

        BrtHistory brtHistory = historyRecordManagerService.convertToNewHistory(history);

        brtService.processCdr(brtHistory);
    }
}
