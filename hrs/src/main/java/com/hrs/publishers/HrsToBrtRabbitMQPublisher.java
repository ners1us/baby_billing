package com.hrs.publishers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrs.dto.BrtHistoryDto;
import com.hrs.models.CallCost;
import com.hrs.models.MonthCost;
import com.hrs.models.MonthCostsMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Отправитель сообщений в RabbitMQ из HRS в BRT.
 */
@Service
@RequiredArgsConstructor
public class HrsToBrtRabbitMQPublisher {

    @Value("${rabbitmq.exchange.brt-hrs.name}")
    private String exchange;

    @Value("${rabbitmq.call.hrs.to.brt.key}")
    private String callRoutingKey;

    @Value("${rabbitmq.month.hrs.to.brt.key}")
    private String monthRoutingKey;

    @NonNull
    private final ObjectMapper objectMapper;

    @NonNull
    private RabbitTemplate rabbitTemplate;

    /**
     * Отправляет данные о месячных затратах на звонки из HRS в BRT.
     *
     * @param monthCosts список объектов MonthCost, представляющих месячные затраты на звонки.
     * @throws JsonProcessingException если произошла ошибка при преобразовании в JSON.
     */
    public void sendMonthCallToBrt(List<MonthCost> monthCosts) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(new MonthCostsMessage(monthCosts));

        rabbitTemplate.convertAndSend(exchange, monthRoutingKey, json);
    }

    /**
     * Отправляет данные о стоимости звонка из HRS в BRT.
     *
     * @param brtHistoryDto объект BrtHistoryDto, представляющий историю звонка.
     * @param cost стоимость звонка.
     * @throws JsonProcessingException если произошла ошибка при преобразовании в JSON.
     */
    public void sendCallCostToBrt(BrtHistoryDto brtHistoryDto, BigDecimal cost) throws JsonProcessingException {
        CallCost callCost = new CallCost(brtHistoryDto.getClient(), brtHistoryDto.getCallerId(), brtHistoryDto.getStartTime(),
                brtHistoryDto.getEndTime(), cost);
        String json = objectMapper.writeValueAsString(callCost);

        rabbitTemplate.convertAndSend(exchange, callRoutingKey, json);
    }
}
