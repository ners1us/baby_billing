package com.cdr_generator.publishers;

import com.cdr_generator.dto.CdrHistoryDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Отправитель записей CDR в RabbitMQ для обработки BRT.
 */
@Service
@RequiredArgsConstructor
public class CdrToBrtRabbitMQPublisher {

    @Value("${rabbitmq.exchange.cdr-brt.name}")
    private String exchange;

    @Value("${rabbitmq.cdr.to.brt.key}")
    private String routingCdrKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(CdrToBrtRabbitMQPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    /**
     * Отправляет сообщение с записью CDR в RabbitMQ.
     *
     * @param cdrHistoryDto запись CDR, которую необходимо отправить.
     */
    public void sendMessage(CdrHistoryDto cdrHistoryDto) {
        LOGGER.info(String.format("Message sent -> %s", cdrHistoryDto.toString()));

        rabbitTemplate.convertAndSend(exchange, routingCdrKey, cdrHistoryDto);
    }
}
