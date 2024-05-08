package com.baby_billing.hrs.services.implementations;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.hrs.entities.HrsHistory;
import com.baby_billing.hrs.entities.Tariffs;
import com.baby_billing.hrs.entities.Traffic;
import com.baby_billing.hrs.models.Limits;
import com.baby_billing.hrs.models.OverLimit;
import com.baby_billing.hrs.models.Prepaid;
import com.baby_billing.hrs.models.TariffRules;
import com.baby_billing.hrs.repositories.IHrsHistoryRepository;
import com.baby_billing.hrs.repositories.ITariffsRepository;
import com.baby_billing.hrs.repositories.ITrafficRepository;
import com.baby_billing.hrs.services.IHrsDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class HrsDatabaseService implements IHrsDatabaseService {

    private ITariffsRepository tariffsRepository;

    private final IHrsHistoryRepository historyRepository;

    private final ITrafficRepository trafficRepository;

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

    public long countTariffs() {
        return tariffsRepository.count();
    }

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

    public Tariffs getTariff(Integer tariffId) {
        return tariffsRepository.findByTariffId(tariffId);
    }

}
