package com.baby_billing.brt.services.implementations;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.entities.Client;
import com.baby_billing.brt.entities.TariffPaymentHistory;
import com.baby_billing.brt.services.IBalanceCalculatorService;
import com.baby_billing.brt.services.IBrtDatabaseService;
import com.baby_billing.brt.services.IBrtService;
import com.baby_billing.brt.services.IBrtHistoryRecordManagerService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Сервис для управления данными в BRT.
 */
@Service
@AllArgsConstructor
public class BrtService implements IBrtService {

    private IBalanceCalculatorService balanceCalculator;

    private IBrtHistoryRecordManagerService historyWriter;

    private IBrtDatabaseService brtDatabaseService;

    /**
     * Инициализация таблицы клиентов.
     * Проверяет наличие данных о клиентах в базе данных и, если они отсутствуют, заполняет базу данными из сервиса BrtDatabaseService.
     */
    @PostConstruct
    public void initialize() {
        if (brtDatabaseService.countClients() == 0) {
            brtDatabaseService.populateBrtClientsData();
        }
    }

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
    public void processCostFromHrs(BrtHistory brtHistory, BigDecimal cost) {
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
