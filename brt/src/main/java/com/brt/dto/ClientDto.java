package com.brt.dto;

import com.brt.entities.Client;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClientDto {

    private String clientId;

    private String password;

    private Integer tariffId;

    private BigDecimal balance;

    public static ClientDto fromEntity(Client client) {
        ClientDto clientDto = new ClientDto();

        clientDto.setClientId(client.getClientId());
        clientDto.setPassword(client.getPassword());
        clientDto.setTariffId(client.getTariffId());
        clientDto.setBalance(client.getBalance());

        return clientDto;
    }

    public static Client toEntity(ClientDto clientDto) {
        Client client = new Client();

        client.setClientId(clientDto.getClientId());
        client.setPassword(clientDto.getPassword());
        client.setTariffId(clientDto.getTariffId());
        client.setBalance(clientDto.getBalance());

        return client;
    }

    public static List<ClientDto> fromEntities(List<Client> clients) {
        return clients.stream()
                .map(ClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    public static List<Client> toEntities(List<ClientDto> clientDtos) {
        return clientDtos.stream()
                .map(ClientDto::toEntity)
                .collect(Collectors.toList());
    }
}
