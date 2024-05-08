package com.baby_billing.brt.consumers;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.services.IBrtService;
import com.baby_billing.hrs.models.CallCost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CallCostsFromHrsRabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallCostsFromHrsRabbitMQConsumer.class);

    private final IBrtService brtService;

    @RabbitListener(queues = {"${rabbitmq.call.hrs.to.brt.queue.name}"})
    public void consumeMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            CallCost callCost = objectMapper.readValue(message, CallCost.class);

            BrtHistory brtHistory = new BrtHistory();
            brtHistory.setClient(callCost.getClient());
            brtHistory.setCallerId(callCost.getCallerId());
            brtHistory.setStartTime(callCost.getStartTime());
            brtHistory.setEndTime(callCost.getEndTime());
            brtService.processCostFromHrs(brtHistory, callCost.getCost());

        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing CallCost to JSON: {}", e.getMessage());
        }

        LOGGER.info(String.format("Consumed message -> %s", message));
    }
}
