package com.marketplace.springboot.Dto;

import com.marketplace.springboot.Enum.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderRecordDto {

    @NotNull(message = "Invalid OrderId: Empty orderId")
    private UUID orderId;

    @NotNull(message = "Invalid Status: Empty status")
    private OrderStatus status;

    @NotNull(message = "Invalid OrderDate: Empty orderDate")
    private LocalDateTime orderDate;
}
