package com.baby_billing.brt.services.implementations;


import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.entities.Client;
import com.baby_billing.brt.entities.TariffPaymentHistory;
import com.baby_billing.brt.repositories.IBrtClientRepository;
import com.baby_billing.brt.repositories.IBrtHistoryRepository;
import com.baby_billing.brt.repositories.ITariffPaymentHistoryRepository;
import com.baby_billing.brt.services.IBrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BrtDatabaseService implements IBrtDatabaseService {

    private IBrtHistoryRepository brtHistoryRepository;

    private IBrtClientRepository brtClientRepository;

    private ITariffPaymentHistoryRepository tariffPaymentHistoryRepository;

    private static final Integer FIRST_TARIFF_TYPE = 11;

    private static final Integer SECOND_TARIFF_TYPE = 12;

    public void saveBrtHistoryToDatabase(BrtHistory brtHistoryList) {
        brtHistoryRepository.save(brtHistoryList);
    }

    public void saveClientToDatabase(Client client) {
        brtClientRepository.save(client);
    }

    public void saveTariffPaymentHistoryToDatabase(TariffPaymentHistory tariffPaymentHistory) {
        tariffPaymentHistoryRepository.save(tariffPaymentHistory);
    }

    public Client findClientById(String clientId) {
        return brtClientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public BrtHistory findBrtHistoryById(Long id) {
        return brtHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find brt history"));
    }

    public void populateBrtClientsData() {
        List<Client> clients = new ArrayList<>();

        clients.add(new Client("7907443733", FIRST_TARIFF_TYPE, new BigDecimal(10000)));
        clients.add(new Client("7902524952", SECOND_TARIFF_TYPE, new BigDecimal(5000)));
        clients.add(new Client("7908534237", FIRST_TARIFF_TYPE, new BigDecimal(12000)));
        clients.add(new Client("7901893628", SECOND_TARIFF_TYPE, new BigDecimal(3000)));
        clients.add(new Client("7909211047", FIRST_TARIFF_TYPE, new BigDecimal(6000)));
        clients.add(new Client("7902103800", SECOND_TARIFF_TYPE, new BigDecimal(8000)));
        clients.add(new Client("7905453942", FIRST_TARIFF_TYPE, new BigDecimal(2000)));
        clients.add(new Client("7901963484", SECOND_TARIFF_TYPE, new BigDecimal(4000)));
        clients.add(new Client("7908999056", FIRST_TARIFF_TYPE, new BigDecimal(500)));
        clients.add(new Client("7903377234", SECOND_TARIFF_TYPE, new BigDecimal(700)));

        brtClientRepository.saveAll(clients);
    }

    public long countClients() {
        return brtClientRepository.count();
    }
}
