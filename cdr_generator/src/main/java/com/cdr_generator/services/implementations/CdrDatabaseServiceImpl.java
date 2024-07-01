package com.cdr_generator.services.implementations;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.repositories.ClientRepository;
import com.cdr_generator.repositories.CdrHistoryRepository;
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

    private CdrHistoryRepository cdrHistoryRepository;

    private ClientRepository clientRepository;

    /**
     * Сохраняет записи CDR в базу данных.
     *
     * @param cdrHistoryList Список записей CDR.
     */
    public void saveCdrToDatabase(List<CdrHistory> cdrHistoryList) {
        cdrHistoryRepository.saveAll(cdrHistoryList);
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
