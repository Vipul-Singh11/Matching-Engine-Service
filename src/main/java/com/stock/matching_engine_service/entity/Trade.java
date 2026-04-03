package com.stock.matching_engine_service.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trade {
    private Long id;

    private Long buyOrderId;

    private Long sellOrderId;

    private String stockSymbol;

    private int quantity;

    private BigDecimal executedPrice;

    private LocalDateTime executionTime;
}
