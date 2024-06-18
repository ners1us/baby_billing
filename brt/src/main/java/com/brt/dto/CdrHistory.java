package com.brt.dto;

import lombok.Data;

@Data
public class CdrHistory {

    private Long id;

    private String type;

    private CdrClient client;

    private CdrClient caller;

    private long startTime;

    private long endTime;

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%d", type, client.getPhoneNumber(), caller.getPhoneNumber(), startTime, endTime);
    }
}
