package com.marketplace.springboot.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MakerteplaceRecordDto(@NotBlank String name, @NotNull BigDecimal value) {
    
}
