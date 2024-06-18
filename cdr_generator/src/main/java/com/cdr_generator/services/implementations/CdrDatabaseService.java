package com.cdr_generator.services.implementations;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.History;
import com.cdr_generator.repositories.IClientRepository;
import com.cdr_generator.repositories.IHistoryRepository;
import com.cdr_generator.services.ICdrDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, отвечающий за работу с базой данных CDR.
 */
@Service
@AllArgsConstructor
public class CdrDatabaseService implements ICdrDatabaseService {

    private IHistoryRepository historyRepository;

    private IClientRepository clientRepository;

    /**
     * Сохраняет записи CDR в базу данных.
     *
     * @param historyList Список записей CDR.
     */
    public void saveCdrToDatabase(List<History> historyList) {
        historyRepository.saveAll(historyList);
    }

    /**
     * Сохраняет клиентов в базу данных.
     *
     * @param clients Список абонентов.
     */
    public void saveClientsToDatabase(List<Client> clients) {
        clientRepository.saveAll(clients);
    }

    /**
     * Заполняет клиентов в базу данных.
     */
    public void populateClientsData() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("79074437331"));
        clients.add(new Client("79025249522"));
        clients.add(new Client("79085342373"));
        clients.add(new Client("79018936284"));
        clients.add(new Client("79092110475"));
        clients.add(new Client("79021038006"));
        clients.add(new Client("79054539427"));
        clients.add(new Client("79019634848"));
        clients.add(new Client("79089990569"));
        clients.add(new Client("79033772341"));
        clients.add(new Client("79029304492"));
        clients.add(new Client("79019034453"));
        clients.add(new Client("79059798744"));
        clients.add(new Client("79022219705"));
        clients.add(new Client("79030696676"));
        clients.add(new Client("79023971367"));
        clients.add(new Client("79045601308"));
        clients.add(new Client("79038879099"));
        clients.add(new Client("79002551751"));
        clients.add(new Client("79007169002"));

        saveClientsToDatabase(clients);
    }

    /**
     * Считывает количество клиентов в базе данных.
     *
     * @return Количество клиентов.
     */
    public long countClients() {
        return clientRepository.count();
    }
}
