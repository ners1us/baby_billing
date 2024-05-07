package com.baby_billing.brt.services;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.entities.Client;
import com.baby_billing.brt.entities.TariffPaymentHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface IBrtDatabaseService {

    void saveBrtHistoryToDatabase(BrtHistory brtHistoryList);

    void saveClientToDatabase(Client client);

    void populateBrtClientsData();

    void saveTariffPaymentHistoryToDatabase(TariffPaymentHistory tariffPaymentHistory);

    BrtHistory findBrtHistoryById(Long id);

    List<BrtHistory> getAllBrtHistories();

    Client findClientById(String clientId);

    BrtHistory findBrtHistoryByAttributes(String client, String callerId, LocalDateTime startTime, LocalDateTime endTime);

    long countClients();
}
