package com.brt.services;

import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.entities.TariffPaymentHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface BrtDatabaseService {

    void saveBrtHistoryToDatabase(BrtHistory brtHistoryList);

    void saveClientToDatabase(Client client);

    void saveTariffPaymentHistoryToDatabase(TariffPaymentHistory tariffPaymentHistory);

    BrtHistory findBrtHistoryById(Long id);

    List<BrtHistory> getAllBrtHistories();

    List<Client> getAllClients();

    Client findClientById(String clientId);

    BrtHistory findBrtHistoryByAttributes(String client, String callerId, LocalDateTime startTime, LocalDateTime endTime);

    long countClients();
}
