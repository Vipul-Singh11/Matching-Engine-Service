package com.stock.matching_engine_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderBookEntryDto {

    private Long orderId;

    private Long userId;

    private BigDecimal price;

    private Integer quantity;
}