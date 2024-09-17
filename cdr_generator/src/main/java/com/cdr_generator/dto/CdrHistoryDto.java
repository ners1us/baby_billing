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

    public static CdrHistoryDto fromEntity(CdrHistory bid) {
        CdrHistoryDto cdrHistoryDto = new CdrHistoryDto();

        cdrHistoryDto.setId(bid.getId());
        cdrHistoryDto.setType(bid.getType());
        cdrHistoryDto.setClient(bid.getClient());
        cdrHistoryDto.setCaller(bid.getCaller());
        cdrHistoryDto.setStartTime(bid.getStartTime());
        cdrHistoryDto.setEndTime(bid.getEndTime());

        return cdrHistoryDto;
    }

    public static CdrHistory toEntity(CdrHistoryDto bidDto) {
        CdrHistory cdrHistory = new CdrHistory();

        cdrHistory.setId(bidDto.getId());
        cdrHistory.setType(bidDto.getType());
        cdrHistory.setClient(bidDto.getClient());
        cdrHistory.setCaller(bidDto.getCaller());
        cdrHistory.setStartTime(bidDto.getStartTime());
        cdrHistory.setEndTime(bidDto.getEndTime());

        return cdrHistory;
    }

    public static List<CdrHistoryDto> fromEntities(List<CdrHistory> bids) {
        return bids.stream()
                .map(CdrHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public static List<CdrHistory> toEntities(List<CdrHistoryDto> bidDtos) {
        return bidDtos.stream()
                .map(CdrHistoryDto::toEntity)
                .collect(Collectors.toList());
    }
}
