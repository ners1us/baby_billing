package com.baby_billing.cdr_generator.services.implementations;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.repositories.IClientRepository;
import com.baby_billing.cdr_generator.services.IRandomGeneratorService;
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
public class RandomGeneratorService implements IRandomGeneratorService {

    @NonNull
    private final IClientRepository clientRepository;

    private final Random random = new Random();

    /**
     * Генерирует случайного клиента из репозитория клиентов.
     *
     * @return Случайный объект Client.
     */
    public Client getRandomClient() {
        List<Client> clients = clientRepository.findAll();

        return clients.get(random.nextInt(clients.size()));
    }

    /**
     * Генерирует случайное время начала вызова в заданном диапазоне времени.
     *
     * @param startTime Начальное время диапазона.
     * @param endTime   Конечное время диапазона.
     * @return Случайное время начала вызова.
     */
    public long generateRandomStartTime(long startTime, long endTime) {
        return startTime + (long) (random.nextDouble() * (endTime - startTime));
    }

    /**
     * Генерирует случайное время окончания вызова в заданном диапазоне продолжительности.
     *
     * @param startTime   Время начала вызова.
     * @param maxDuration Максимальная продолжительность вызова.
     * @return Случайное время окончания вызова.
     */
    public long generateRandomEndTime(long startTime, long maxDuration) {
        return startTime + (long) (random.nextDouble() * maxDuration);
    }

    /**
     * Генерирует случайное количество вызовов в диапазоне от 1 до 10.
     *
     * @return Случайное количество вызовов.
     */
    public int generateRandomNumberOfCalls() {
        return random.nextInt(10) + 1;
    }
}
