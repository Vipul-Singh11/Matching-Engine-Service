package com.stock.matching_engine_service.dto;

import com.stock.matching_engine_service.enums.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderEventDto {

    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

    @NotNull(message = "Price cannot be null")
    private BigDecimal price;

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    // 🔥 VERY IMPORTANT for FIFO
    private LocalDateTime timestamp;
}