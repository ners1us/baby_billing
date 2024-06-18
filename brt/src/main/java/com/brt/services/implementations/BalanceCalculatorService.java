package com.brt.services.implementations;

import com.brt.entities.Client;
import com.brt.repositories.IBrtClientRepository;
import com.brt.services.IBalanceCalculatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Сервис для вычисления баланса клиента.
 */
@Service
@AllArgsConstructor
public class BalanceCalculatorService implements IBalanceCalculatorService {

    private IBrtClientRepository clientRepository;

    /**
     * Вычисляет баланс клиента на основе стоимости вызовов и обновляет его в репозитории клиентов.
     *
     * @param clientId Номер клиента.
     * @param cost     Стоимость вызовов для вычета из баланса.
     */
    public void calculateClientBalance(String clientId, BigDecimal cost) {
        Client client = clientRepository.findById(clientId).orElse(null);

        if (client == null) {
            throw new RuntimeException("Client not found");
        } else {
            BigDecimal newBalance = client.getBalance().subtract(cost);
            client.setBalance(newBalance);

            clientRepository.save(client);
        }
    }
}
