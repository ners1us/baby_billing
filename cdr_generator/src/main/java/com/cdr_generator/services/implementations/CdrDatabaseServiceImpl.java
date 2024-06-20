package com.cdr_generator.services.implementations;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.History;
import com.cdr_generator.repositories.ClientRepository;
import com.cdr_generator.repositories.HistoryRepository;
import com.cdr_generator.services.CdrDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис, отвечающий за работу с базой данных CDR.
 */
@Service
@AllArgsConstructor
public class CdrDatabaseServiceImpl implements CdrDatabaseService {

    private HistoryRepository historyRepository;

    private ClientRepository clientRepository;

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
     * Считывает количество клиентов в базе данных.
     *
     * @return Количество клиентов.
     */
    public long countClients() {
        return clientRepository.count();
    }
}
