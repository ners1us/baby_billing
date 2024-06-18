package com.brt.services.implementations;

import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.entities.TariffPaymentHistory;
import com.brt.repositories.IBrtClientRepository;
import com.brt.repositories.IBrtHistoryRepository;
import com.brt.repositories.ITariffPaymentHistoryRepository;
import com.brt.services.IBrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для взаимодействия с базой данных BRT.
 */
@Service
@AllArgsConstructor
public class BrtDatabaseService implements IBrtDatabaseService {

    private IBrtHistoryRepository brtHistoryRepository;

    private IBrtClientRepository brtClientRepository;

    private ITariffPaymentHistoryRepository tariffPaymentHistoryRepository;

    // Первый тип тарифа
    private static final Integer FIRST_TARIFF_TYPE = 11;

    // Второй тип тарифа
    private static final Integer SECOND_TARIFF_TYPE = 12;

    /**
     * Сохраняет запись BRT в базу данных.
     *
     * @param brtHistoryList Запись BRT.
     */
    public void saveBrtHistoryToDatabase(BrtHistory brtHistoryList) {
        brtHistoryRepository.save(brtHistoryList);
    }

    /**
     * Сохраняет клиента в базу данных.
     *
     * @param client Клиент.
     */
    public void saveClientToDatabase(Client client) {
        brtClientRepository.save(client);
    }

    /**
     * Сохраняет историю платежей за тарифы в базу данных.
     *
     * @param tariffPaymentHistory История платежей за тарифы.
     */
    public void saveTariffPaymentHistoryToDatabase(TariffPaymentHistory tariffPaymentHistory) {
        tariffPaymentHistoryRepository.save(tariffPaymentHistory);
    }

    /**
     * Находит клиента по его номеру.
     *
     * @param clientId Номер клиента.
     * @return Клиент, найденный в базе данных.
     * @throws RuntimeException если клиент не найден.
     */
    public Client findClientById(String clientId) {
        return brtClientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    /**
     * Находит запись BRT по ее идентификатору.
     *
     * @param id Идентификатор записи BRT.
     * @return Запись BRT, найденная в базе данных.
     * @throws RuntimeException если запись не найдена.
     */
    public BrtHistory findBrtHistoryById(Long id) {
        return brtHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find brt history"));
    }

    /**
     * Находит запись BRT по заданным атрибутам.
     *
     * @param client    Номер телефона клиента.
     * @param callerId  Номер телефона звонящего.
     * @param startTime Время начала звонка.
     * @param endTime   Время окончания звонка.
     * @return Запись BRT, найденная в базе данных.
     */
    public BrtHistory findBrtHistoryByAttributes(String client, String callerId, LocalDateTime startTime, LocalDateTime endTime) {
        return brtHistoryRepository.findByClientAndCallerIdAndStartTimeAndEndTime(client, callerId, startTime, endTime);
    }

    /**
     * Возвращает список всех записей BRT из базы данных.
     *
     * @return Список всех записей BRT.
     */
    public List<BrtHistory> getAllBrtHistories() {
        return brtHistoryRepository.findAll();
    }

    /**
     * Возвращает список всех клиентов из базы данных.
     *
     * @return Список всех клиентов.
     */
    public List<Client> getAllClients() {
        return brtClientRepository.findAll();
    }

    /**
     * Заполняет базу данных клиентами с определенными номерами, тарифами и балансами.
     */
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

    /**
     * Возвращает количество клиентов в базе данных.
     *
     * @return Количество клиентов.
     */
    public long countClients() {
        return brtClientRepository.count();
    }
}
