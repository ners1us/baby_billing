package com.hrs.services.implementations;

import com.hrs.dto.BrtHistory;
import com.hrs.entities.HrsHistory;
import com.hrs.entities.Tariffs;
import com.hrs.entities.Traffic;
import com.hrs.repositories.HrsHistoryRepository;
import com.hrs.repositories.TariffsRepository;
import com.hrs.repositories.TrafficRepository;
import com.hrs.services.HrsDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для взаимодействия с базой данных HRS.
 */
@Service
@AllArgsConstructor
public class HrsDatabaseServiceImpl implements HrsDatabaseService {

    private TariffsRepository tariffsRepository;

    private final HrsHistoryRepository historyRepository;

    private final TrafficRepository trafficRepository;

    /**
     * Возвращает количество тарифов в базе данных.
     *
     * @return Количество тарифов.
     */
    public long countTariffs() {
        return tariffsRepository.count();
    }

    /**
     * Сохраняет данные о звонке в базу данных HRS.
     *
     * @param brtHistory    Данные о звонке от BRT.
     * @param duration      Продолжительность звонка в минутах.
     * @param cost          Стоимость звонка.
     * @param currentMonth  Текущий месяц.
     */
    public void saveCallData(BrtHistory brtHistory, long duration, BigDecimal cost, int currentMonth) {
        List<HrsHistory> existingHistories = historyRepository.findByClientIdAndCallerIdAndStartTimeAndEndTime(
                brtHistory.getClient(), brtHistory.getCallerId(), brtHistory.getStartTime(), brtHistory.getEndTime());

        if (!existingHistories.isEmpty()) {
            for (HrsHistory existingHistory : existingHistories) {
                existingHistory.setCost(cost);
                existingHistory.setDuration(duration);
                historyRepository.save(existingHistory);
            }
        } else {
            HrsHistory history = new HrsHistory(brtHistory.getClient(), brtHistory.getCallerId(), brtHistory.getStartTime(),
                    brtHistory.getEndTime(), brtHistory.getTariffId(), brtHistory.getInternal(), cost, duration);
            historyRepository.save(history);
        }

        Traffic traffic = trafficRepository.findByClientId(brtHistory.getClient());
        if (traffic == null) {
            traffic = new Traffic(brtHistory.getClient(), brtHistory.getTariffId(), currentMonth);
        }

        long minutesExtCurrentMonth = traffic.getMinutesExtCurrentMonth() != null ? traffic.getMinutesExtCurrentMonth() : 0L;
        long minutesIntCurrentMonth = traffic.getMinutesIntCurrentMonth() != null ? traffic.getMinutesIntCurrentMonth() : 0L;

        if (brtHistory.getInternal()) {
            traffic.setMinutesIntCurrentMonth(minutesIntCurrentMonth + duration);
        } else {
            traffic.setMinutesExtCurrentMonth(minutesExtCurrentMonth + duration);
        }
        trafficRepository.save(traffic);
    }

    /**
     * Возвращает тариф по его идентификатору.
     *
     * @param tariffId Идентификатор тарифа.
     * @return Тариф.
     */
    public Tariffs getTariff(Integer tariffId) {
        return tariffsRepository.findByTariffId(tariffId);
    }

}
