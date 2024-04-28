package com.baby_billing.cdr_generator.services.implementations;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.services.ICdrService;
import com.baby_billing.cdr_generator.services.IDatabaseService;
import com.baby_billing.cdr_generator.services.IFileManagerService;
import com.baby_billing.cdr_generator.services.IRandomGeneratorService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class CdrService implements ICdrService {

    private IDatabaseService databaseService;
    private IRandomGeneratorService randomGeneratorService;
    private IFileManagerService fileManagerService;

    private static final int MAX_CALLS_PER_FILE = 10;
    private static final long MAX_DURATION_PER_CALL = 3600;

    @PostConstruct
    public void initialize() {
        if (databaseService.countClients() == 0) {
            databaseService.populateClientsData();
        }
    }

    public void processCdr(List<History> historyList) {
        databaseService.saveCdrToDatabase(historyList);

        List<List<History>> files = fileManagerService.splitIntoFiles(historyList, MAX_CALLS_PER_FILE);
        for (int i = 0; i < files.size(); i++) {
            String fileName = "data/cdr_" + (i + 1) + ".txt";
            fileManagerService.saveCdrToFile(files.get(i), fileName);
        }
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<List<History>> generateCdr() {
        List<History> historyList = new ArrayList<>();
        long startTime = 1617235200L;
        long endTime = startTime + 31536000L;

        while (startTime < endTime) {
            int numCalls = randomGeneratorService.generateRandomNumberOfCalls();
            List<History> monthHistory = generateCdrForMonth(startTime, numCalls);
            historyList.addAll(monthHistory);
            startTime += 2592000L;
        }

        return CompletableFuture.completedFuture(historyList);
    }

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
