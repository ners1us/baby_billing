package com.brt.dto;

import lombok.Data;

@Data
public class CdrHistoryDto {

    private Long id;

    private String type;

    private CdrClientDto client;

    private CdrClientDto caller;

    private long startTime;

    private long endTime;

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%d", type, client.getPhoneNumber(), caller.getPhoneNumber(), startTime, endTime);
    }
}
