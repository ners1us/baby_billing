package com.baby_billing.hrs.publishers;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.hrs.models.CallCost;
import com.baby_billing.hrs.models.MonthCost;
import com.baby_billing.hrs.models.MonthCostsMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * @param monthCosts Список объектов MonthCost, представляющих месячные затраты на звонки.
     * @throws JsonProcessingException если произошла ошибка при преобразовании в JSON.
     */
    public void sendMonthCallToBrt(List<MonthCost> monthCosts) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(new MonthCostsMessage(monthCosts));

        rabbitTemplate.convertAndSend(exchange, monthRoutingKey, json);
    }

    /**
     * Отправляет данные о стоимости звонка из HRS в BRT.
     *
     * @param brtHistory Объект BrtHistory, представляющий историю звонка.
     * @param cost       Стоимость звонка.
     * @throws JsonProcessingException если произошла ошибка при преобразовании в JSON.
     */
    public void sendCallCostToBrt(BrtHistory brtHistory, BigDecimal cost) throws JsonProcessingException {
        CallCost callCost = new CallCost(brtHistory.getClient(), brtHistory.getCallerId(), brtHistory.getStartTime(),
                brtHistory.getEndTime(), cost);
        String json = objectMapper.writeValueAsString(callCost);

        rabbitTemplate.convertAndSend(exchange, callRoutingKey, json);
    }
}
