package com.cdr_generator.services.implementations;

import com.cdr_generator.entities.Client;
import com.cdr_generator.repositories.ClientRepository;
import com.cdr_generator.services.RandomGeneratorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Сервис для генерации случайных данных, связанных с CDR.
 */
@Service
@RequiredArgsConstructor
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

    @NonNull
    private final ClientRepository clientRepository;

    private final Random random = new Random();

    /**
     * Генерирует случайного клиента из репозитория клиентов.
     *
     * @return случайный объект Client.
     */
    public Client getRandomClient() {
        List<Client> clients = clientRepository.findAll();

        return clients.get(random.nextInt(clients.size()));
    }

    /**
     * Генерирует случайное время начала вызова в заданном диапазоне времени.
     *
     * @param startTime начальное время диапазона.
     * @param endTime конечное время диапазона.
     * @return случайное время начала вызова.
     */
    public long generateRandomStartTime(long startTime, long endTime) {
        return startTime + (long) (random.nextDouble() * (endTime - startTime));
    }

    /**
     * Генерирует случайное время окончания вызова в заданном диапазоне продолжительности.
     *
     * @param startTime время начала вызова.
     * @param maxDuration максимальная продолжительность вызова.
     * @return случайное время окончания вызова.
     */
    public long generateRandomEndTime(long startTime, long maxDuration) {
        return startTime + (long) (random.nextDouble() * maxDuration);
    }

    /**
     * Генерирует случайное количество вызовов в диапазоне от 1 до 10.
     *
     * @return случайное количество вызовов.
     */
    public int generateRandomNumberOfCalls() {
        return random.nextInt(10) + 1;
    }
}
