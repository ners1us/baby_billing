package com.brt.services.implementations;

import com.brt.dto.CdrHistory;
import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.repositories.BrtClientRepository;
import com.brt.services.BrtDatabaseService;
import com.brt.services.BrtHistoryRecordManagerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Сервис для управления записями истории в BRT.
 */
@Service
@AllArgsConstructor
public class BrtHistoryRecordManagerServiceImpl implements BrtHistoryRecordManagerService {

    private BrtClientRepository clientRepository;

    private BrtDatabaseService brtDatabaseService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BrtHistoryRecordManagerServiceImpl.class);

    /**
     * Обогащает запись истории перед сохранением в базу данных BRT.
     *
     * @param brtHistory Запись истории для обогащения.
     */
    public void enrichHistory(BrtHistory brtHistory) {
        Client client = clientRepository.findById(brtHistory.getClient()).orElse(null);

        if (client == null) {
            LOGGER.error(String.format("Client not found for history: %s", brtHistory));
        } else {
            brtHistory.setTariffId(client.getTariffId());
            boolean internalCall = clientRepository.existsById(brtHistory.getCallerId());
            brtHistory.setInternal(internalCall);

            brtDatabaseService.saveBrtHistoryToDatabase(brtHistory);
        }
    }

    /**
     * Преобразует историю вызова из формата CDR в формат BRT.
     *
     * @param oldHistory История вызова в формате CDR.
     * @return Новая запись истории в формате BRT.
     */
    public BrtHistory convertToNewHistory(CdrHistory oldHistory) {
        BrtHistory newHistory = new BrtHistory();

        newHistory.setType(oldHistory.getType());
        newHistory.setClient(oldHistory.getClient().getPhoneNumber());
        newHistory.setCallerId(oldHistory.getCaller().getPhoneNumber());
        newHistory.setStartTime(LocalDateTime.ofEpochSecond(oldHistory.getStartTime(), 0, ZoneOffset.UTC));
        newHistory.setEndTime(LocalDateTime.ofEpochSecond(oldHistory.getEndTime(), 0, ZoneOffset.UTC));

        return newHistory;
    }

}
