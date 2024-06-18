package com.hrs.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrs.dto.BrtHistory;
import com.hrs.entities.HrsHistory;
import com.hrs.entities.Tariffs;
import com.hrs.entities.Traffic;
import com.hrs.models.MonthCost;
import com.hrs.models.TariffRules;
import com.hrs.publishers.HrsToBrtRabbitMQPublisher;
import com.hrs.repositories.IHrsHistoryRepository;
import com.hrs.repositories.ITrafficRepository;
import com.hrs.services.ICallCostCalculator;
import com.hrs.services.IHrsDatabaseService;
import com.hrs.services.IHrsService;
import jakarta.annotation.PostConstruct;
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
public class HrsService implements IHrsService {

    @NonNull
    private final ICallCostCalculator callCostCalculator;

    @NonNull
    private final IHrsHistoryRepository historyRepository;

    @NonNull
    private final ITrafficRepository trafficRepository;

    @NonNull
    private final ObjectMapper objectMapper;

    @NonNull
    private final IHrsDatabaseService hrsDatabaseService;

    @NonNull
    private final HrsToBrtRabbitMQPublisher hrsToBrtRabbitMQPublisher;

    private int currentMonth;

    /**
     * Инициализация таблицы тарифов.
     * Проверяет наличие данных о тарифах в базе данных и, если они отсутствуют, заполняет базу данными из сервиса HrsDatabaseService.
     */
    @PostConstruct
    public void init() {
        if (hrsDatabaseService.countTariffs() == 0) {
            hrsDatabaseService.populateHrsTariffsData();
        }
    }

    /**
     * Обрабатывает звонки от BRT.
     *
     * @param json JSON-представление данных о звонке от BRT.
     * @throws JsonProcessingException если происходит ошибка при обработке JSON.
     */
    public void processCallsFromBrt(String json) throws JsonProcessingException {
        BrtHistory brtHistory = objectMapper.readValue(json, BrtHistory.class);

        HrsHistory history = new HrsHistory(brtHistory.getClient(), brtHistory.getCallerId(), brtHistory.getStartTime(), brtHistory.getEndTime(), brtHistory.getTariffId(), brtHistory.getInternal());
        historyRepository.save(history);

        int month = brtHistory.getEndTime().getMonthValue();

        if (month != currentMonth) {
            processMonthChange(brtHistory.getEndTime());
            currentMonth = month;
        }

        long duration = callCostCalculator.calculateDuration(brtHistory.getStartTime(), brtHistory.getEndTime());
        Tariffs tariff = hrsDatabaseService.getTariff(brtHistory.getTariffId());

        TariffRules tariffRules = tariff.getTariffRules();
        BigDecimal cost = callCostCalculator.calculateCallCost(brtHistory, tariffRules, duration);
        hrsDatabaseService.saveCallData(brtHistory, duration, cost, currentMonth);

        hrsToBrtRabbitMQPublisher.sendCallCostToBrt(brtHistory, cost);
    }

    /**
     * Обрабатывает смену месяца.
     *
     * @param endTime Время окончания звонка, определяющее текущий месяц.
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
