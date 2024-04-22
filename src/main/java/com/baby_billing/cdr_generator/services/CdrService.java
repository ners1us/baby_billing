package com.baby_billing.cdr_generator.services;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.repositories.IClientRepository;
import com.baby_billing.cdr_generator.repositories.IHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class CdrService implements ICdrService {

    private IClientRepository clientRepository;
    private IHistoryRepository historyRepository;

    private static final int MAX_CALLS_PER_FILE = 10;
    private static final long MAX_DURATION_PER_CALL = 3600;

    public void processCdr(List<History> historyList) {
        saveCdrToDatabase(historyList);

        List<List<History>> files = splitIntoFiles(historyList);
        for (int i = 0; i < files.size(); i++) {
            String fileName = "data/cdr_" + (i + 1) + ".txt";
            saveCdrToFile(files.get(i), fileName);
        }
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<List<History>> generateCdr() {
        List<History> historyList = new ArrayList<>();
        Random random = new Random();
        long startTime = 1617235200L;
        long endTime = startTime + 31536000L;

        while (startTime < endTime) {
            int numCalls = random.nextInt(10) + 1;
            List<History> monthHistory = generateCdrForMonth(startTime, numCalls, random);
            historyList.addAll(monthHistory);
            startTime += 2592000L;
        }

        return CompletableFuture.completedFuture(historyList);
    }

    private List<History> generateCdrForMonth(long startTime, int numCalls, Random random) {
        List<History> monthHistory = new ArrayList<>();

        for (int i = 0; i < numCalls; i++) {
            History outgoingCdr = generateRandomCall(startTime, random);
            History incomingCdr = generateReverseCall(outgoingCdr);

            monthHistory.add(outgoingCdr);
            monthHistory.add(incomingCdr);
        }

        monthHistory.sort(Comparator.comparingLong(History::getEndTime));
        return monthHistory;
    }

    private History generateRandomCall(long startTime, Random random) {
        Client client = getRandomClient();
        Client caller = getRandomClient();
        long callStartTime = startTime + (long) (random.nextDouble() * 2592000L);
        long callEndTime = callStartTime + (long) (random.nextDouble() * MAX_DURATION_PER_CALL);

        while (client.equals(caller)) {
            client = getRandomClient();
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

    private void saveCdrToDatabase(List<History> historyList) {
        historyRepository.saveAll(historyList);
    }

    private void saveCdrToFile(List<History> historyList, String fileName) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            for (History history : historyList) {
                writer.write(history.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Client getRandomClient() {
        List<Client> clients = clientRepository.findAll();
        Random random = new Random();
        return clients.get(random.nextInt(clients.size()));
    }

    private List<List<History>> splitIntoFiles(List<History> historyList) {
        List<List<History>> files = new ArrayList<>();
        List<History> currentFile = new ArrayList<>();
        int count = 0;

        for (History history : historyList) {
            currentFile.add(history);
            count++;

            if (count == MAX_CALLS_PER_FILE) {
                files.add(new ArrayList<>(currentFile));
                currentFile.clear();
                count = 0;
            }
        }

        if (!currentFile.isEmpty()) {
            files.add(new ArrayList<>(currentFile));
        }

        return files;
    }
}
