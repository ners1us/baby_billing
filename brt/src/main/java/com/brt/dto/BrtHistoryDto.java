package com.brt.dto;

import com.brt.entities.BrtHistory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BrtHistoryDto {
    private Long id;

    private String type;

    private String client;

    private String callerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer tariffId;

    private Boolean internal;

    private BigDecimal cost;

    public static BrtHistoryDto fromEntity(BrtHistory brtHistory) {
        BrtHistoryDto brtHistoryDto = new BrtHistoryDto();

        brtHistoryDto.setId(brtHistory.getId());
        brtHistoryDto.setType(brtHistory.getType());
        brtHistoryDto.setClient(brtHistory.getClient());
        brtHistoryDto.setCallerId(brtHistory.getCallerId());
        brtHistoryDto.setStartTime(brtHistory.getStartTime());
        brtHistoryDto.setEndTime(brtHistory.getEndTime());
        brtHistoryDto.setTariffId(brtHistory.getTariffId());
        brtHistoryDto.setInternal(brtHistory.getInternal());
        brtHistoryDto.setCost(brtHistory.getCost());

        return brtHistoryDto;
    }

    public static BrtHistory toEntity(BrtHistoryDto brtHistoryDto) {
        BrtHistory brtHistory = new BrtHistory();

        brtHistory.setId(brtHistory.getId());
        brtHistory.setType(brtHistory.getType());
        brtHistory.setClient(brtHistory.getClient());
        brtHistory.setCallerId(brtHistory.getCallerId());
        brtHistory.setStartTime(brtHistory.getStartTime());
        brtHistory.setEndTime(brtHistory.getEndTime());
        brtHistory.setTariffId(brtHistory.getTariffId());
        brtHistory.setInternal(brtHistory.getInternal());
        brtHistory.setCost(brtHistory.getCost());

        return brtHistory;
    }

    public static List<BrtHistoryDto> fromEntities(List<BrtHistory> brtHistories) {
        return brtHistories.stream()
                .map(BrtHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public static List<BrtHistory> toEntities(List<BrtHistoryDto> brtHistoryDtos) {
        return brtHistoryDtos.stream()
                .map(BrtHistoryDto::toEntity)
                .collect(Collectors.toList());
    }
}
