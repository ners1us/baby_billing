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
import java.time.LocalDateTime;
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

    public BrtHistory findBrtHistoryByAttributes(String client, String callerId, LocalDateTime startTime, LocalDateTime endTime) {
        return brtHistoryRepository.findByClientAndCallerIdAndStartTimeAndEndTime(client, callerId, startTime, endTime);
    }

    public List<BrtHistory> getAllBrtHistories() {
        return brtHistoryRepository.findAll();
    }

    public List<Client> getAllClients() {
        return brtClientRepository.findAll();
    }

    public void populateBrtClientsData() {
        List<Client> clients = new ArrayList<>();

        clients.add(new Client("79074437331", FIRST_TARIFF_TYPE, new BigDecimal(10000)));
        clients.add(new Client("79025249522", SECOND_TARIFF_TYPE, new BigDecimal(5000)));
        clients.add(new Client("79085342373", FIRST_TARIFF_TYPE, new BigDecimal(12000)));
        clients.add(new Client("79018936284", SECOND_TARIFF_TYPE, new BigDecimal(3000)));
        clients.add(new Client("79092110475", FIRST_TARIFF_TYPE, new BigDecimal(6000)));
        clients.add(new Client("79021038006", SECOND_TARIFF_TYPE, new BigDecimal(8000)));
        clients.add(new Client("79054539427", FIRST_TARIFF_TYPE, new BigDecimal(2000)));
        clients.add(new Client("79019634848", SECOND_TARIFF_TYPE, new BigDecimal(4000)));
        clients.add(new Client("79089990569", FIRST_TARIFF_TYPE, new BigDecimal(500)));
        clients.add(new Client("79033772341", SECOND_TARIFF_TYPE, new BigDecimal(700)));

        brtClientRepository.saveAll(clients);
    }

    public long countClients() {
        return brtClientRepository.count();
    }
}
