package com.cdr_generator.services.implementations;

import com.cdr_generator.entities.Client;
import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.exceptions.FailedOpeningCdrFileException;
import com.cdr_generator.exceptions.FailedWritingCdrHistoryToFileException;
import com.cdr_generator.services.FileManagerService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис для работы с файлами, связанными с CDR.
 */
@Service
public class FileManagerServiceImpl implements FileManagerService {

    /**
     * Проверяет существование и очищает папку данных.
     */
    public void checkAndCleanDataFolder(){
        File dataFolder = new File("data");

        if (dataFolder.isDirectory()) {
            File[] files = Objects.requireNonNull(dataFolder.listFiles());

            Stream.of(files)
                    .forEach(File::delete);
        } else {
            dataFolder.mkdir();
        }
    }

    /**
     * Считывает историю из файлов.
     *
     * @return Список объектов History, считанных из файлов.
     * @throws IOException В случае ошибки ввода-вывода при чтении файлов.
     */
    public List<CdrHistory> readHistoryFromFile() throws IOException {
        List<CdrHistory> cdrHistoryList = new ArrayList<>();
        File dataFolder = new File("data");

        if (dataFolder.isDirectory()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String line;

                            while ((line = br.readLine()) != null) {
                                String[] parts = line.split(",");

                                if (parts.length == 5) {
                                    CdrHistory cdrHistory = new CdrHistory();
                                    cdrHistory.setType(parts[0]);
                                    cdrHistory.setClient(parseClient(parts[1]));
                                    cdrHistory.setCaller(parseClient(parts[2]));
                                    cdrHistory.setStartTime(Long.parseLong(parts[3]));
                                    cdrHistory.setEndTime(Long.parseLong(parts[4]));
                                    cdrHistoryList.add(cdrHistory);
                                }
                            }
                        }
                    }
                }
            }
        }
        return cdrHistoryList;
    }

    /**
     * Разбивает список истории на файлы с заданным количеством вызовов.
     *
     * @param cdrHistoryList     Список истории, который необходимо разделить.
     * @param maxCallsPerFile Максимальное количество вызовов в одном файле.
     * @return Список списков истории, разбитой на файлы.
     */
    public List<List<CdrHistory>> splitIntoFiles(List<CdrHistory> cdrHistoryList, int maxCallsPerFile) {
        AtomicInteger count = new AtomicInteger(0);

        return new ArrayList<>(cdrHistoryList.stream()
                .collect(Collectors.groupingBy(it -> count.getAndIncrement() / maxCallsPerFile))
                .values());
    }

    /**
     * Сохраняет CDR в файл.
     *
     * @param cdrHistoryList Список объектов History, которые необходимо сохранить.
     * @param fileName    Имя файла, в который нужно сохранить CDR.
     */
    public void saveCdrToFile(List<CdrHistory> cdrHistoryList, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            cdrHistoryList.stream()
                    .map(CdrHistory::toString)
                    .forEach(cdrString -> {
                        try {
                            writer.write(cdrString + "\n");
                        } catch (IOException ex) {
                            throw new FailedWritingCdrHistoryToFileException("Failed writing cdr history to file", ex);
                        }
                    });
        } catch (IOException ex) {
            throw new FailedOpeningCdrFileException("Failed writing cdr history to file", ex);
        }
    }

    /**
     * Преобразует строковое представление номера телефона в объект Client.
     *
     * @param phone Строковое представление номера телефона.
     * @return Объект Client с указанным номером телефона.
     */
    private Client parseClient(String phone) {
        Client client = new Client();

        client.setPhoneNumber(phone);

        return client;
    }
}
