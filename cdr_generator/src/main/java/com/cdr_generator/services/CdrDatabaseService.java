package com.cdr_generator.services;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.CdrHistory;

import java.util.List;

public interface CdrDatabaseService {

    void saveCdrToDatabase(List<CdrHistory> cdrHistoryList);

    void saveClientsToDatabase(List<Client> clients);

    long countClients();
}
