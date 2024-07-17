package com.brt.services.implementations;

import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.entities.TariffPaymentHistory;
import com.brt.exceptions.NotFoundBrtHistoryException;
import com.brt.exceptions.NotFoundClientException;
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
     * @param brtHistoryList запись BRT.
     */
    public void saveBrtHistoryToDatabase(BrtHistory brtHistoryList) {
        brtHistoryRepository.save(brtHistoryList);
    }

    /**
     * Сохраняет клиента в базу данных.
     *
     * @param client клиент.
     */
    public void saveClientToDatabase(Client client) {
        brtClientRepository.save(client);
    }

    /**
     * Сохраняет историю платежей за тарифы в базу данных.
     *
     * @param tariffPaymentHistory история платежей за тарифы.
     */
    public void saveTariffPaymentHistoryToDatabase(TariffPaymentHistory tariffPaymentHistory) {
        tariffPaymentHistoryRepository.save(tariffPaymentHistory);
    }

    /**
     * Находит клиента по его номеру.
     *
     * @param clientId номер клиента.
     * @return клиент, найденный в базе данных.
     * @throws NotFoundBrtHistoryException если клиент не найден.
     */
    public Client findClientById(String clientId) {
        return brtClientRepository.findById(clientId).orElseThrow(() -> new NotFoundClientException("Client not found"));
    }

    /**
     * Находит запись BRT по ее идентификатору.
     *
     * @param id идентификатор записи BRT.
     * @return запись BRT, найденная в базе данных.
     * @throws NotFoundBrtHistoryException если запись не найдена.
     */
    public BrtHistory findBrtHistoryById(Long id) {
        return brtHistoryRepository.findById(id).orElseThrow(() -> new NotFoundBrtHistoryException("Could not find brt history"));
    }

    /**
     * Находит запись BRT по заданным атрибутам.
     *
     * @param client номер телефона клиента.
     * @param callerId номер телефона звонящего.
     * @param startTime время начала звонка.
     * @param endTime время окончания звонка.
     * @throws NotFoundBrtHistoryException если запись не найдена.
     * @return запись BRT, найденная в базе данных.
     */
    public BrtHistory findBrtHistoryByAttributes(String client, String callerId, LocalDateTime startTime, LocalDateTime endTime) {
        return brtHistoryRepository.findByClientAndCallerIdAndStartTimeAndEndTime(client, callerId, startTime, endTime).orElseThrow(() -> new NotFoundBrtHistoryException("Could not find brt history"));
    }

    /**
     * Возвращает список всех записей BRT из базы данных.
     *
     * @return список всех записей BRT.
     */
    public List<BrtHistory> getAllBrtHistories() {
        return brtHistoryRepository.findAll();
    }

    /**
     * Возвращает список всех клиентов из базы данных.
     *
     * @return список всех клиентов.
     */
    public List<Client> getAllClients() {
        return brtClientRepository.findAll();
    }
}
