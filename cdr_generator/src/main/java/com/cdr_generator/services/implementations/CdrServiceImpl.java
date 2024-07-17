package com.cdr_generator.services.implementations;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.exceptions.FailedSavingCdrToFileException;
import com.cdr_generator.exceptions.FailedWritingCdrHistoryToFileException;
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
 * Сервис для генерации и обработки CDR.
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
     * @param cdrHistoryList список объектов History, содержащих информацию о звонках.
     * @throws FailedSavingCdrToFileException если произошла ошибка во время записи истории CDR.
     */
    public void processCdr(List<CdrHistory> cdrHistoryList) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        futures.add(CompletableFuture.runAsync(() -> databaseService.saveCdrToDatabase(cdrHistoryList)));
        futures.add(CompletableFuture.runAsync(() -> {
            List<List<CdrHistory>> files = fileManagerService.splitIntoFiles(cdrHistoryList, MAX_CALLS_PER_FILE);
            for (int i = 0; i < files.size(); i++) {
                String fileName = "data/cdr_" + (i + 1) + ".txt";
                try {
                    fileManagerService.saveCdrToFile(files.get(i), fileName);
                } catch (FailedWritingCdrHistoryToFileException ex) {
                    throw new FailedSavingCdrToFileException("Failed saving CDR file", ex);
                }
            }
        }));

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * Асинхронно генерирует CDR для всех клиентов в течение года.
     *
     * @return CompletableFuture, содержащий список объектов History, представляющих сгенерированные звонки.
     */
    @Async("asyncTaskExecutor")
    public CompletableFuture<List<CdrHistory>> generateCdr() {
        List<CdrHistory> cdrHistoryList = new ArrayList<>();

        long startTime = 1680307200L;
        long endTime = startTime + 31536000L;

        while (startTime < endTime) {
            int numCalls = randomGeneratorService.generateRandomNumberOfCalls();
            List<CdrHistory> monthCdrHistory = generateCdrForMonth(startTime, numCalls);
            cdrHistoryList.addAll(monthCdrHistory);
            startTime += 2592000L;
        }

        return CompletableFuture.completedFuture(cdrHistoryList);
    }

    /**
     * Читает и возвращает историю звонков из файла.
     *
     * @return список объектов History, содержащих информацию о звонках.
     * @throws IOException если возникает ошибка ввода-вывода при чтении файла.
     */
    public List<CdrHistory> readHistory() throws IOException {
        return fileManagerService.readHistoryFromFile();
    }

    /**
     * Проверяет наличие и очищает папку с данными.
     *
     * @throws IOException если возникает ошибка ввода-вывода при доступе к файлам или папкам.
     */
    public void checkAndCleanData() throws IOException {
        fileManagerService.checkAndCleanDataFolder();
    }

    /**
     * Генерирует историю звонков CDR за месяц.
     *
     * @param startTime начальное время для генерации звонков.
     * @param numCalls количество звонков, которые нужно сгенерировать (каждый звонок и обратный ему звонок).
     * @return список CdrHistory, представляющий историю звонков за месяц, отсортированный по времени завершения звонков.
     */
    private List<CdrHistory> generateCdrForMonth(long startTime, int numCalls) {
        List<CdrHistory> monthCdrHistory = new ArrayList<>();

        for (int i = 0; i < numCalls; i++) {
            CdrHistory outgoingCdr = generateRandomCall(startTime);
            CdrHistory incomingCdr = generateReverseCall(outgoingCdr);

            monthCdrHistory.add(outgoingCdr);
            monthCdrHistory.add(incomingCdr);
        }

        monthCdrHistory.sort(Comparator.comparingLong(CdrHistory::getEndTime));
        return monthCdrHistory;
    }

    /**
     * Генерирует случайный исходящий звонок для указанного времени начала звонка.
     *
     * @param startTime время начала генерации звонка.
     * @return CdrHistory, представляющий исходящий звонок.
     */
    private CdrHistory generateRandomCall(long startTime) {
        Client client = randomGeneratorService.getRandomClient();
        Client caller = randomGeneratorService.getRandomClient();

        long callStartTime = randomGeneratorService.generateRandomStartTime(startTime, startTime + 2592000L);
        long callEndTime = randomGeneratorService.generateRandomEndTime(callStartTime, MAX_DURATION_PER_CALL);

        while (client.equals(caller)) {
            client = randomGeneratorService.getRandomClient();
        }

        CdrHistory outgoingCdr = new CdrHistory();
        outgoingCdr.setType("01");
        outgoingCdr.setClient(client);
        outgoingCdr.setCaller(caller);
        outgoingCdr.setStartTime(callStartTime);
        outgoingCdr.setEndTime(callEndTime);

        return outgoingCdr;
    }

    /**
     * Генерирует входящий звонок на основе исходящего звонка.
     *
     * @param outgoingCdr объект CdrHistory, представляющий исходящий звонок.
     * @return CdrHistory, представляющий входящий звонок.
     */
    private CdrHistory generateReverseCall(CdrHistory outgoingCdr) {
        CdrHistory incomingCdr = new CdrHistory();

        incomingCdr.setType("02");
        incomingCdr.setClient(outgoingCdr.getCaller());
        incomingCdr.setCaller(outgoingCdr.getClient());
        incomingCdr.setStartTime(outgoingCdr.getStartTime());
        incomingCdr.setEndTime(outgoingCdr.getEndTime());

        return incomingCdr;
    }
}
