package com.hrs.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrs.dto.BrtHistoryDto;
import com.hrs.entities.HrsHistory;
import com.hrs.entities.Tariffs;
import com.hrs.entities.Traffic;
import com.hrs.models.MonthCost;
import com.hrs.models.TariffRules;
import com.hrs.publishers.HrsToBrtRabbitMQPublisher;
import com.hrs.repositories.HrsHistoryRepository;
import com.hrs.repositories.TrafficRepository;
import com.hrs.services.CallCostCalculatorService;
import com.hrs.services.HrsDatabaseService;
import com.hrs.services.HrsProcessorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для обработки данных в HRS.
 */
@Service
@RequiredArgsConstructor
public class HrsProcessorServiceImpl implements HrsProcessorService {

    @NonNull
    private final CallCostCalculatorService callCostCalculatorService;

    @NonNull
    private final HrsHistoryRepository historyRepository;

    @NonNull
    private final TrafficRepository trafficRepository;

    @NonNull
    private final ObjectMapper objectMapper;

    @NonNull
    private final HrsDatabaseService hrsDatabaseService;

    @NonNull
    private final HrsToBrtRabbitMQPublisher hrsToBrtRabbitMQPublisher;

    private int currentMonth;

    /**
     * Обрабатывает звонки от BRT.
     *
     * @param json JSON-представление данных о звонке от BRT.
     * @throws JsonProcessingException если происходит ошибка при обработке JSON.
     */
    public void processCallsFromBrt(String json) throws JsonProcessingException {
        BrtHistoryDto brtHistoryDto = objectMapper.readValue(json, BrtHistoryDto.class);

        HrsHistory history = new HrsHistory(brtHistoryDto.getClient(), brtHistoryDto.getCallerId(), brtHistoryDto.getStartTime(), brtHistoryDto.getEndTime(), brtHistoryDto.getTariffId(), brtHistoryDto.getInternal());
        historyRepository.save(history);

        int month = brtHistoryDto.getEndTime().getMonthValue();

        if (month != currentMonth) {
            processMonthChange(brtHistoryDto.getEndTime());
            currentMonth = month;
        }

        long duration = callCostCalculatorService.calculateDuration(brtHistoryDto.getStartTime(), brtHistoryDto.getEndTime());
        Tariffs tariff = hrsDatabaseService.getTariff(brtHistoryDto.getTariffId());

        TariffRules tariffRules = tariff.getTariffRules();
        BigDecimal cost = callCostCalculatorService.calculateCallCost(brtHistoryDto, tariffRules, duration);

        hrsDatabaseService.saveCallData(brtHistoryDto, duration, cost, currentMonth);

        hrsToBrtRabbitMQPublisher.sendCallCostToBrt(brtHistoryDto, cost);
    }

    /**
     * Обрабатывает смену месяца.
     *
     * @param endTime время окончания звонка, определяющее текущий месяц.
     * @throws JsonProcessingException если происходит ошибка при обработке JSON.
     */
    private void processMonthChange(LocalDateTime endTime) throws JsonProcessingException {
        List<Traffic> trafficList = trafficRepository.findAll();
        List<MonthCost> monthCosts = new ArrayList<>();

        for (Traffic traffic : trafficList) {
            Tariffs tariff = hrsDatabaseService.getTariff(traffic.getTariffId());
            TariffRules tariffRules = tariff.getTariffRules();

            if (tariffRules.getPrepaid() != null) {
                BigDecimal cost = tariffRules.getPrepaid().getTariffCost();
                monthCosts.add(new MonthCost(traffic.getClientId(), traffic.getTariffId(), cost, endTime));
            }

            traffic.setMonth(currentMonth);
            traffic.setMinutesIntCurrentMonth(0L);
            traffic.setMinutesExtCurrentMonth(0L);
            trafficRepository.save(traffic);
        }

        if (!monthCosts.isEmpty()) {
            hrsToBrtRabbitMQPublisher.sendMonthCallToBrt(monthCosts);
        }
    }
}
