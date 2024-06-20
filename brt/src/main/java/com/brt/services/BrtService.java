package com.brt.services;

import com.brt.entities.BrtHistory;
import com.brt.entities.TariffPaymentHistory;
import com.brt.exceptions.NotFoundClientException;

import java.math.BigDecimal;

public interface BrtService {

    void processCdr(BrtHistory brtHistory);

    void processCostFromHrs(BrtHistory brtHistory, BigDecimal cost) throws NotFoundClientException;

    void processTariffChangeFromHrs(TariffPaymentHistory tariffPaymentHistory) throws NotFoundClientException;
}
