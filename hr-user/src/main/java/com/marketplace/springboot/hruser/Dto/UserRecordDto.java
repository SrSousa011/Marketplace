package com.marketplace.springboot.hruser.Dto;

import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecordDto  {


    private UUID userId;

    @NotBlank(message = "Invalid Username: Empty username")
    @Size(min = 3, max = 30, message = "Invalid Name: Must be of 3 - 30 characters")
    private String username;

    @NotBlank(message = "Invalid Password: Empty password")
    @Size(min = 3, max = 30, message = "Invalid password: Must be of 3 - 30 characters")
    private String password;

    @Email
    @Size(min = 8, max = 30, message = "Invalid Email: Must be of 3 - 30 characters")
    private String email;

    private LocalDateTime createdAt;
}

