package com.cdr_generator.dto;

import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.entities.Client;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CdrHistoryDto {
    private Long id;

    private String type;

    private Client client;

    private Client caller;

    private long startTime;

    private long endTime;

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%d", type, client.getPhoneNumber(), caller.getPhoneNumber(), startTime, endTime);
    }

    public static CdrHistoryDto fromEntity(CdrHistory cdrHistory) {
        CdrHistoryDto cdrHistoryDto = new CdrHistoryDto();

        cdrHistoryDto.setId(cdrHistory.getId());
        cdrHistoryDto.setType(cdrHistory.getType());
        cdrHistoryDto.setClient(cdrHistory.getClient());
        cdrHistoryDto.setCaller(cdrHistory.getCaller());
        cdrHistoryDto.setStartTime(cdrHistory.getStartTime());
        cdrHistoryDto.setEndTime(cdrHistory.getEndTime());

        return cdrHistoryDto;
    }

    public static CdrHistory toEntity(CdrHistoryDto cdrHistoryDto) {
        CdrHistory cdrHistory = new CdrHistory();

        cdrHistory.setId(cdrHistoryDto.getId());
        cdrHistory.setType(cdrHistoryDto.getType());
        cdrHistory.setClient(cdrHistoryDto.getClient());
        cdrHistory.setCaller(cdrHistoryDto.getCaller());
        cdrHistory.setStartTime(cdrHistoryDto.getStartTime());
        cdrHistory.setEndTime(cdrHistoryDto.getEndTime());

        return cdrHistory;
    }

    public static List<CdrHistoryDto> fromEntities(List<CdrHistory> cdrHistories) {
        return cdrHistories.stream()
                .map(CdrHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public static List<CdrHistory> toEntities(List<CdrHistoryDto> cdrHistoryDtos) {
        return cdrHistoryDtos.stream()
                .map(CdrHistoryDto::toEntity)
                .collect(Collectors.toList());
    }
}
