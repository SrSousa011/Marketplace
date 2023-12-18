package com.marketplace.springboot.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRecordDto {
        private UUID productId;

        @NotBlank(message = "Invalid Name: Empty name")
        @Size(min = 3, max = 30, message = "Invalid Name: Must be of 3 - 30 characters")
        private String name;

        @NotNull(message = "Invalid Price: Null price")
        @DecimalMin(value = "0.1", inclusive = true, message = "Invalid Price: Must be greater than or equal to 0.1")
        private BigDecimal price;

        private Integer quantityAvailable;

        @NotBlank(message = "Invalid description: Empty description")
        private String description;

        private LocalDateTime createdAt;
}
