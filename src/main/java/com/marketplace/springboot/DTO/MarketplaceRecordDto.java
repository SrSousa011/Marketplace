package com.marketplace.springboot.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record MarketplaceRecordDto(
        UUID productId,
        @NotBlank(message = "Invalid Name: Empty name")
        @Size(min = 3, max = 30, message = "Invalid Name: Must be of 3 - 30 characters")
        String name,
        @NotNull(message = "Invalid Price: Null price")
        @DecimalMin(value = "0.1", inclusive = true, message = "Invalid Price: Must be greater than or equal to 0.1")
        BigDecimal price,
        @Email(message = "Invalid email")
        String email,
        @NotBlank(message = "Invalid password: Empty password")
        String password,
        @NotNull(message = "Invalid createdAt: Null createdAt")
        LocalDateTime createdAt
) {
}