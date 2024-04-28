package com.baby_billing.cdr_generator.services;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.entities.History;

import java.util.List;

public interface IDatabaseService {

    void saveCdrToDatabase(List<History> historyList);

    void saveClientsToDatabase(List<Client> clients);

    void populateClientsData();

    long countClients();
}
