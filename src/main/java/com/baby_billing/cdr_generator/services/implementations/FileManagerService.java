package com.baby_billing.cdr_generator.services.implementations;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.services.IFileManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с файлами, связанными с CDR.
 */
@Service
public class FileManagerService implements IFileManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagerService.class);

    /**
     * Проверяет существование и очищает папку данных.
     */
    public void checkAndCleanDataFolder(){
        File dataFolder = new File("data");

        if (dataFolder.isDirectory()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
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
    public List<History> readHistoryFromFile() throws IOException {
        List<History> historyList = new ArrayList<>();
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
                                    History history = new History();
                                    history.setType(parts[0]);
                                    history.setClient(parseClient(parts[1]));
                                    history.setCaller(parseClient(parts[2]));
                                    history.setStartTime(Long.parseLong(parts[3]));
                                    history.setEndTime(Long.parseLong(parts[4]));
                                    historyList.add(history);
                                }
                            }
                        }
                    }
                }
            }
        }
        return historyList;
    }

    /**
     * Разбивает список истории на файлы с заданным количеством вызовов.
     *
     * @param historyList     Список истории, который необходимо разделить.
     * @param maxCallsPerFile Максимальное количество вызовов в одном файле.
     * @return Список списков истории, разбитой на файлы.
     */
    public List<List<History>> splitIntoFiles(List<History> historyList, int maxCallsPerFile) {
        List<List<History>> files = new ArrayList<>();
        List<History> currentFile = new ArrayList<>();
        int count = 0;

        for (History history : historyList) {
            currentFile.add(history);
            count++;

            if (count == maxCallsPerFile) {
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

    /**
     * Сохраняет CDR в файл.
     *
     * @param historyList Список объектов History, которые необходимо сохранить.
     * @param fileName    Имя файла, в который нужно сохранить CDR.
     */
    public void saveCdrToFile(List<History> historyList, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (History history : historyList) {
                writer.write(history.toString() + "\n");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
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
