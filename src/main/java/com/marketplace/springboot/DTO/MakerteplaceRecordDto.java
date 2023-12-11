package com.marketplace.springboot.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record MakerteplaceRecordDto(
        @NotBlank(message = "Invalid Name: Empty name")
        @Size(min = 3, max = 30, message = "Invalid Name: Must be of 3 - 30 characters")
        String name,
        @NotBlank(message = "Invalid Price: Empty price")
        @DecimalMin(value = "0.1", message = "Invalid Price: Must be greater than or equal to 0.1")
        @NotNull BigDecimal price

    //@Email(message = "Invalid email")
    //String email;
    //@NotBlank(message = "Invalid Phone number: Empty number")
    //@NotNull(message = "Invalid Phone number: Number is NULL")
    //@Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    //String mobile;
    //@Min(value = 1, message = "Invalid Age: Equals to zero or Less than zero")
    //@Max(value = 100, message = "Invalid Age: Exceeds 100 years")
    //Integer age;
) {
}
