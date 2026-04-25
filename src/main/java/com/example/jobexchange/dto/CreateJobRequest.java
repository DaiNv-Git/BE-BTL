package com.example.jobexchange.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateJobRequest(
        @NotBlank String title,
        @NotBlank String companyName,
        @NotBlank String location,
        @NotBlank String salaryRange,
        @NotBlank String description,
        @NotBlank String requirements
) {
}
