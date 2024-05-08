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

@Service
@AllArgsConstructor
public class BrtService implements IBrtService {

    private IBalanceCalculatorService balanceCalculator;

    private IBrtHistoryRecordManagerService historyWriter;

    private IBrtDatabaseService brtDatabaseService;

    @PostConstruct
    public void initialize() {
        if (brtDatabaseService.countClients() == 0) {
            brtDatabaseService.populateBrtClientsData();
        }
    }

    public void processCdr(BrtHistory brtHistory) {
        historyWriter.enrichHistory(brtHistory);
    }

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
