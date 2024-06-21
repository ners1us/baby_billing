package com.brt.services.implementations;

import com.brt.entities.Client;
import com.brt.exceptions.NotFoundClientException;
import com.brt.repositories.BrtClientRepository;
import com.brt.services.BalanceCalculatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Сервис для вычисления баланса клиента.
 */
@Service
@AllArgsConstructor
public class BalanceCalculatorServiceImpl implements BalanceCalculatorService {

    private BrtClientRepository clientRepository;

    /**
     * Вычисляет баланс клиента на основе стоимости вызовов и обновляет его в репозитории клиентов.
     *
     * @param clientId Номер клиента.
     * @param cost     Стоимость вызовов для вычета из баланса.
     */
    public void calculateClientBalance(String clientId, BigDecimal cost) throws NotFoundClientException {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new NotFoundClientException("Client not found"));

        BigDecimal newBalance = client.getBalance().subtract(cost);
        client.setBalance(newBalance);

        clientRepository.save(client);
    }
}
