package com.stock.matching_engine_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderBookDto {

    private String symbol;

    private List<OrderBookEntryDto> buyOrders;

    private List<OrderBookEntryDto> sellOrders;
}