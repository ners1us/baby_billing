package com.brt.services;

import com.brt.entities.BrtHistory;
import com.brt.entities.TariffPaymentHistory;

import java.math.BigDecimal;

public interface BrtProcessorService {

    void processCdr(BrtHistory brtHistory);

    void processCostFromHrs(BrtHistory brtHistory, BigDecimal cost);

    void processTariffChangeFromHrs(TariffPaymentHistory tariffPaymentHistory);
}
