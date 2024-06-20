package com.brt.services.implementations;

import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.entities.TariffPaymentHistory;
import com.brt.repositories.BrtClientRepository;
import com.brt.repositories.BrtHistoryRepository;
import com.brt.repositories.TariffPaymentHistoryRepository;
import com.brt.services.BrtDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для взаимодействия с базой данных BRT.
 */
@Service
@AllArgsConstructor
public class BrtDatabaseServiceImpl implements BrtDatabaseService {

    private BrtHistoryRepository brtHistoryRepository;

    private BrtClientRepository brtClientRepository;

    private TariffPaymentHistoryRepository tariffPaymentHistoryRepository;

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
     * Возвращает количество клиентов в базе данных.
     *
     * @return Количество клиентов.
     */
    public long countClients() {
        return brtClientRepository.count();
    }
}
