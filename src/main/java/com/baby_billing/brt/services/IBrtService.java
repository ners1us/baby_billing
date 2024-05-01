package com.baby_billing.brt.services;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.entities.TariffPaymentHistory;

import java.math.BigDecimal;

public interface IBrtService {

    void processCdr(BrtHistory brtHistory);

    void processCostFromHrs(BrtHistory brtHistory, BigDecimal cost);

    void processTariffChangeFromHrs(TariffPaymentHistory tariffPaymentHistory);
}
