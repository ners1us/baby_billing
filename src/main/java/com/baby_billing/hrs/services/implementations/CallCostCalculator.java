package com.baby_billing.hrs.services.implementations;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.hrs.models.TariffRules;
import com.baby_billing.hrs.services.ICallCostCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Сервис для вычисления стоимости звонка.
 */
@Service
public class CallCostCalculator implements ICallCostCalculator {

    /**
     * Вычисляет продолжительность звонка в минутах.
     *
     * @param startTime Время начала звонка.
     * @param endTime   Время окончания звонка.
     * @return Продолжительность звонка в минутах.
     */
    public long calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        long duration = ChronoUnit.MINUTES.between(startTime, endTime);

        return Math.max(duration, 1);
    }

    /**
     * Вычисляет стоимость звонка на основе истории звонка и правил тарификации.
     *
     * @param brtHistory   Объект BrtHistory, представляющий историю звонка.
     * @param tariffRules  Правила тарификации для данного звонка.
     * @param duration     Продолжительность звонка в минутах.
     * @return Стоимость звонка.
     */
    public BigDecimal calculateCallCost(BrtHistory brtHistory, TariffRules tariffRules, long duration) {
        if (brtHistory.getInternal()) {
            if (brtHistory.getType().equals("01")) {
                if (tariffRules.getOverlimit() != null && tariffRules.getOverlimit().getInternalOutcoming() != null) {
                    return BigDecimal.valueOf(duration).multiply(tariffRules.getOverlimit().getInternalOutcoming());
                } else {
                    return BigDecimal.ZERO;
                }
            } else {
                if (tariffRules.getOverlimit() != null && tariffRules.getOverlimit().getInternalIncoming() != null) {
                    return BigDecimal.valueOf(duration).multiply(tariffRules.getOverlimit().getInternalIncoming());
                } else {
                    return BigDecimal.ZERO;
                }
            }
        } else {
            if (brtHistory.getType().equals("01")) {
                if (tariffRules.getOverlimit() != null && tariffRules.getOverlimit().getExternalOutcoming() != null) {
                    return BigDecimal.valueOf(duration).multiply(tariffRules.getOverlimit().getExternalOutcoming());
                } else {
                    return BigDecimal.ZERO;
                }
            } else {
                if (tariffRules.getOverlimit() != null && tariffRules.getOverlimit().getExternalIncoming() != null) {
                    return BigDecimal.valueOf(duration).multiply(tariffRules.getOverlimit().getExternalIncoming());
                } else {
                    return BigDecimal.ZERO;
                }
            }
        }
    }
}
