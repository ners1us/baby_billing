package com.cdr_generator.services;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.History;

import java.util.List;

public interface ICdrDatabaseService {

    void saveCdrToDatabase(List<History> historyList);

    void saveClientsToDatabase(List<Client> clients);

    void populateClientsData();

    long countClients();
}
