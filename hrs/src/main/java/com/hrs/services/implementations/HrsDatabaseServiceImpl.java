package com.hrs.services.implementations;

import com.hrs.dto.BrtHistoryDto;
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
     * @param brtHistoryDto    Данные о звонке от BRT.
     * @param duration      Продолжительность звонка в минутах.
     * @param cost          Стоимость звонка.
     * @param currentMonth  Текущий месяц.
     */
    public void saveCallData(BrtHistoryDto brtHistoryDto, long duration, BigDecimal cost, int currentMonth) {
        List<HrsHistory> existingHistories = historyRepository.findByClientIdAndCallerIdAndStartTimeAndEndTime(
                brtHistoryDto.getClient(), brtHistoryDto.getCallerId(), brtHistoryDto.getStartTime(), brtHistoryDto.getEndTime());

        if (!existingHistories.isEmpty()) {
            for (HrsHistory existingHistory : existingHistories) {
                existingHistory.setCost(cost);
                existingHistory.setDuration(duration);
                historyRepository.save(existingHistory);
            }
        } else {
            HrsHistory history = new HrsHistory(brtHistoryDto.getClient(), brtHistoryDto.getCallerId(), brtHistoryDto.getStartTime(),
                    brtHistoryDto.getEndTime(), brtHistoryDto.getTariffId(), brtHistoryDto.getInternal(), cost, duration);
            historyRepository.save(history);
        }

        Traffic traffic = trafficRepository.findByClientId(brtHistoryDto.getClient());
        if (traffic == null) {
            traffic = new Traffic(brtHistoryDto.getClient(), brtHistoryDto.getTariffId(), currentMonth);
        }

        long minutesExtCurrentMonth = traffic.getMinutesExtCurrentMonth() != null ? traffic.getMinutesExtCurrentMonth() : 0L;
        long minutesIntCurrentMonth = traffic.getMinutesIntCurrentMonth() != null ? traffic.getMinutesIntCurrentMonth() : 0L;

        if (brtHistoryDto.getInternal()) {
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
