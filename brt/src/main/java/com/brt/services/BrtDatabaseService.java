package com.brt.services;

import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.entities.TariffPaymentHistory;
import com.brt.exceptions.NotFoundBrtHistoryException;
import com.brt.exceptions.NotFoundClientException;

import java.time.LocalDateTime;
import java.util.List;

public interface BrtDatabaseService {

    void saveBrtHistoryToDatabase(BrtHistory brtHistoryList);

    void saveClientToDatabase(Client client);

    void saveTariffPaymentHistoryToDatabase(TariffPaymentHistory tariffPaymentHistory);

    BrtHistory findBrtHistoryById(Long id) throws NotFoundBrtHistoryException;

    List<BrtHistory> getAllBrtHistories();

    List<Client> getAllClients();

    Client findClientById(String clientId) throws NotFoundClientException;

    BrtHistory findBrtHistoryByAttributes(String client, String callerId, LocalDateTime startTime, LocalDateTime endTime);

    long countClients();
}
