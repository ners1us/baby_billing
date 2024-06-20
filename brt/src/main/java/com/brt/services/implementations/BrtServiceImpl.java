package com.brt.services.implementations;

import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.entities.TariffPaymentHistory;
import com.brt.exceptions.NotFoundClientException;
import com.brt.services.BalanceCalculatorService;
import com.brt.services.BrtDatabaseService;
import com.brt.services.BrtHistoryRecordManagerService;
import com.brt.services.BrtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Сервис для управления данными в BRT.
 */
@Service
@AllArgsConstructor
public class BrtServiceImpl implements BrtService {

    private BalanceCalculatorService balanceCalculator;

    private BrtHistoryRecordManagerService historyWriter;

    private BrtDatabaseService brtDatabaseService;

    /**
     * Обрабатывает запись истории вызова CDR.
     *
     * @param brtHistory Запись истории вызова в формате BRT.
     */
    public void processCdr(BrtHistory brtHistory) {
        historyWriter.enrichHistory(brtHistory);
    }

    /**
     * Обрабатывает стоимость вызова из сообщения HRS.
     * Обновляет запись истории в базе данных BRT, вычисляет и обновляет баланс клиента.
     *
     * @param brtHistory Запись истории вызова в формате BRT.
     * @param cost       Стоимость вызова.
     */
    public void processCostFromHrs(BrtHistory brtHistory, BigDecimal cost) throws NotFoundClientException {
        BrtHistory existingHistory = brtDatabaseService.findBrtHistoryByAttributes(brtHistory.getClient(), brtHistory.getCallerId(), brtHistory.getStartTime(), brtHistory.getEndTime());

        BigDecimal existingCost = existingHistory.getCost().add(cost);

        existingHistory.setCost(existingCost);
        existingHistory.setStartTime(brtHistory.getStartTime());
        existingHistory.setEndTime(brtHistory.getEndTime());

        brtDatabaseService.saveBrtHistoryToDatabase(existingHistory);

        TariffPaymentHistory paymentHistory = new TariffPaymentHistory();
        paymentHistory.setClientId(existingHistory.getClient());
        paymentHistory.setTariffId(existingHistory.getTariffId());
        paymentHistory.setCost(cost);
        paymentHistory.setTime(existingHistory.getEndTime());

        brtDatabaseService.saveTariffPaymentHistoryToDatabase(paymentHistory);

        balanceCalculator.calculateClientBalance(brtHistory.getClient(), cost);
    }

    /**
     * Обрабатывает изменение тарифа из сообщения HRS.
     * Обновляет запись о платеже за тариф в базе данных BRT.
     *
     * @param tariffPaymentHistory Запись о платеже за тариф.
     */
    public void processTariffChangeFromHrs(TariffPaymentHistory tariffPaymentHistory) {
        brtDatabaseService.saveTariffPaymentHistoryToDatabase(tariffPaymentHistory);

        Client client = brtDatabaseService.findClientById(tariffPaymentHistory.getClientId());
        if (client == null) {
            throw new RuntimeException("Client not found");
        } else {
            client.setTariffId(tariffPaymentHistory.getTariffId());

            brtDatabaseService.saveClientToDatabase(client);
        }
    }
}
