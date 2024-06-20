package com.cdr_generator.services.implementations;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.History;
import com.cdr_generator.services.CdrDatabaseService;
import com.cdr_generator.services.CdrService;
import com.cdr_generator.services.FileManagerService;
import com.cdr_generator.services.RandomGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Сервис для генерации и обработки CDR (Call Detail Record).
 * CDR содержит информацию о звонках, такую как клиент, время начала и окончания звонка и т. д.
 */
@Service
@AllArgsConstructor
public class CdrServiceImpl implements CdrService {

    private CdrDatabaseService databaseService;

    private RandomGeneratorService randomGeneratorService;

    private FileManagerService fileManagerService;

    // Максимальное количество звонков в одном файле CDR
    private static final int MAX_CALLS_PER_FILE = 10;
    // Максимальная продолжительность одного звонка в секундах
    private static final long MAX_DURATION_PER_CALL = 3600;

    /**
     * Обрабатывает CDR и сохраняет данные в базу данных и файлы.
     *
     * @param historyList Список объектов History, содержащих информацию о звонках
     */
    public void processCdr(List<History> historyList) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        futures.add(CompletableFuture.runAsync(() -> databaseService.saveCdrToDatabase(historyList)));
        futures.add(CompletableFuture.runAsync(() -> {
            List<List<History>> files = fileManagerService.splitIntoFiles(historyList, MAX_CALLS_PER_FILE);
            for (int i = 0; i < files.size(); i++) {
                String fileName = "data/cdr_" + (i + 1) + ".txt";
                fileManagerService.saveCdrToFile(files.get(i), fileName);
            }
        }));

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * Асинхронно генерирует CDR для всех клиентов в течение года.
     *
     * @return CompletableFuture, содержащий список объектов History, представляющих сгенерированные звонки
     */
    @Async("asyncTaskExecutor")
    public CompletableFuture<List<History>> generateCdr() {
        List<History> historyList = new ArrayList<>();

        long startTime = 1680307200L;
        long endTime = startTime + 31536000L;

        while (startTime < endTime) {
            int numCalls = randomGeneratorService.generateRandomNumberOfCalls();
            List<History> monthHistory = generateCdrForMonth(startTime, numCalls);
            historyList.addAll(monthHistory);
            startTime += 2592000L;
        }

        return CompletableFuture.completedFuture(historyList);
    }

    /**
     * Читает и возвращает историю звонков из файла.
     *
     * @return Список объектов History, содержащих информацию о звонках
     * @throws IOException если возникает ошибка ввода-вывода при чтении файла
     */
    public List<History> readHistory() throws IOException {
        return fileManagerService.readHistoryFromFile();
    }

    /**
     * Проверяет наличие и очищает папку с данными.
     *
     * @throws IOException если возникает ошибка ввода-вывода при доступе к файлам или папкам
     */
    public void checkAndCleanData() throws IOException {
        fileManagerService.checkAndCleanDataFolder();
    }

    // Методы генерации звонков

    private List<History> generateCdrForMonth(long startTime, int numCalls) {
        List<History> monthHistory = new ArrayList<>();

        for (int i = 0; i < numCalls; i++) {
            History outgoingCdr = generateRandomCall(startTime);
            History incomingCdr = generateReverseCall(outgoingCdr);

            monthHistory.add(outgoingCdr);
            monthHistory.add(incomingCdr);
        }

        monthHistory.sort(Comparator.comparingLong(History::getEndTime));
        return monthHistory;
    }

    private History generateRandomCall(long startTime) {
        Client client = randomGeneratorService.getRandomClient();
        Client caller = randomGeneratorService.getRandomClient();

        long callStartTime = randomGeneratorService.generateRandomStartTime(startTime, startTime + 2592000L);
        long callEndTime = randomGeneratorService.generateRandomEndTime(callStartTime, MAX_DURATION_PER_CALL);

        while (client.equals(caller)) {
            client = randomGeneratorService.getRandomClient();
        }

        History outgoingCdr = new History();
        outgoingCdr.setType("01");
        outgoingCdr.setClient(client);
        outgoingCdr.setCaller(caller);
        outgoingCdr.setStartTime(callStartTime);
        outgoingCdr.setEndTime(callEndTime);

        return outgoingCdr;
    }

    private History generateReverseCall(History outgoingCdr) {
        History incomingCdr = new History();

        incomingCdr.setType("02");
        incomingCdr.setClient(outgoingCdr.getCaller());
        incomingCdr.setCaller(outgoingCdr.getClient());
        incomingCdr.setStartTime(outgoingCdr.getStartTime());
        incomingCdr.setEndTime(outgoingCdr.getEndTime());

        return incomingCdr;
    }
}
