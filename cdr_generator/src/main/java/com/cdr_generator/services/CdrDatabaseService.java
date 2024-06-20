package com.cdr_generator.services;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.History;

import java.util.List;

public interface CdrDatabaseService {

    void saveCdrToDatabase(List<History> historyList);

    void saveClientsToDatabase(List<Client> clients);

    long countClients();
}
