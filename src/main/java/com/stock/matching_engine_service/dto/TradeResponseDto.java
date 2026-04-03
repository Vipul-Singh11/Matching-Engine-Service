package com.stock.matching_engine_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TradeResponseDto {

    private Long tradeId;

    private Long buyOrderId;

    private Long sellOrderId;

    private String stockSymbol;

    private int quantity;

    private BigDecimal price;

    private LocalDateTime executionTime;
}