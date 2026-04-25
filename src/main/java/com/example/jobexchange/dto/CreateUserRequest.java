package com.example.jobexchange.dto;

import com.example.jobexchange.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotBlank String phone,
        String password,
        @NotNull UserRole role,
        String organizationName,
        String headline
) {
}
