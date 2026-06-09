package com.stock.matching_engine_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TradeEventDto {

    private Long tradeId;
    private Long buyOrderId;
    private Long sellOrderId;
    private Long buyerUserId;
    private Long sellerUserId;
    private String stockSymbol;
    private int quantity;
    private BigDecimal price;
    private LocalDateTime executionTime;
}