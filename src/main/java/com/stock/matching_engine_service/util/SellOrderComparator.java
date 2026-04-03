package com.stock.matching_engine_service.util;

import com.stock.matching_engine_service.dto.OrderEventDto;

import java.util.Comparator;

public class SellOrderComparator implements Comparator<OrderEventDto> {

    @Override
    public int compare(OrderEventDto o1, OrderEventDto o2) {

        // 🔥 1. Lower price first (ASC)
        int priceComparison = o1.getPrice().compareTo(o2.getPrice());

        if (priceComparison != 0) {
            return priceComparison;
        }

        // 🔥 2. FIFO → earlier timestamp first
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}