package com.hrs.services.implementations;

import com.hrs.dto.BrtHistory;
import com.hrs.entities.HrsHistory;
import com.hrs.entities.Tariffs;
import com.hrs.entities.Traffic;
import com.hrs.models.Limits;
import com.hrs.models.OverLimit;
import com.hrs.models.Prepaid;
import com.hrs.models.TariffRules;
import com.hrs.repositories.IHrsHistoryRepository;
import com.hrs.repositories.ITariffsRepository;
import com.hrs.repositories.ITrafficRepository;
import com.hrs.services.IHrsDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для взаимодействия с базой данных HRS.
 */
@Service
@AllArgsConstructor
public class HrsDatabaseService implements IHrsDatabaseService {

    private ITariffsRepository tariffsRepository;

    private final IHrsHistoryRepository historyRepository;

    private final ITrafficRepository trafficRepository;

    /**
     * Заполняет базу данных информацией о тарифах HRS.
     */
    public void populateHrsTariffsData() {
        Tariffs tariff11 = new Tariffs();
        tariff11.setTariffId(11);
        TariffRules rules11 = new TariffRules();
        rules11.setName("Классика");
        rules11.setDescription("Входящие - бесплатно. Исходящие внутри ромашки - 1,5 у.е/мин, для остальных - 2,5 у.е./мин");
        rules11.setCurrency("RUB");
        OverLimit overLimit11 = new OverLimit();
        overLimit11.setInternalIncoming(BigDecimal.valueOf(0.00));
        overLimit11.setInternalOutcoming(BigDecimal.valueOf(1.50));
        overLimit11.setExternalIncoming(BigDecimal.valueOf(0.00));
        overLimit11.setExternalOutcoming(BigDecimal.valueOf(2.50));
        rules11.setOverlimit(overLimit11);
        tariff11.setTariffRules(rules11);

        tariffsRepository.save(tariff11);

        Tariffs tariff12 = new Tariffs();
        tariff12.setTariffId(12);
        TariffRules rules12 = new TariffRules();
        rules12.setName("Помесячный");
        rules12.setDescription("Лимит 50 минут на все звонки, сверх лимита - по тарифу Классика");
        rules12.setCurrency("RUB");
        Prepaid prepaid12 = new Prepaid();
        prepaid12.setTariffCost(BigDecimal.valueOf(100));
        Limits limits12 = new Limits();
        limits12.setTotalMinutes(50L);
        prepaid12.setLimits(limits12);
        rules12.setPrepaid(prepaid12);
        OverLimit overLimit12 = new OverLimit();
        overLimit12.setReferenceTariffId(11);
        rules12.setOverlimit(overLimit12);
        tariff12.setTariffRules(rules12);

        tariffsRepository.save(tariff12);
    }

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