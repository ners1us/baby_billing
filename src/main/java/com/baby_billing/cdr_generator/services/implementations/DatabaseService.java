package com.baby_billing.cdr_generator.services.implementations;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.repositories.IClientRepository;
import com.baby_billing.cdr_generator.repositories.IHistoryRepository;
import com.baby_billing.cdr_generator.services.IDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DatabaseService implements IDatabaseService {

    private IHistoryRepository historyRepository;
    private IClientRepository clientRepository;

    public void saveCdrToDatabase(List<History> historyList) {
        historyRepository.saveAll(historyList);
    }

    public void saveClientsToDatabase(List<Client> clients) {
        clientRepository.saveAll(clients);
    }

    public void populateClientsData() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("7907443733"));
        clients.add(new Client("7902524952"));
        clients.add(new Client("7908534237"));
        clients.add(new Client("7901893628"));
        clients.add(new Client("7909211047"));
        clients.add(new Client("7902103800"));
        clients.add(new Client("7905453942"));
        clients.add(new Client("7901963484"));
        clients.add(new Client("7908999056"));
        clients.add(new Client("7903377234"));
        clients.add(new Client("7902930449"));
        clients.add(new Client("7901903445"));
        clients.add(new Client("7905979874"));
        clients.add(new Client("7902221970"));
        clients.add(new Client("7903069667"));
        clients.add(new Client("7902397136"));
        clients.add(new Client("7904560130"));
        clients.add(new Client("7903887909"));
        clients.add(new Client("7900255175"));
        clients.add(new Client("7900716900"));

        saveClientsToDatabase(clients);
    }

    public long countClients() {
        return clientRepository.count();
    }
}
