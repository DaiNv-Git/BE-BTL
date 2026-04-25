package com.example.jobexchange.dto;

import jakarta.validation.constraints.NotBlank;

public record SaveCvRequest(
        @NotBlank String title,
        @NotBlank String desiredPosition,
        @NotBlank String experienceLevel,
        @NotBlank String summary,
        @NotBlank String skills,
        @NotBlank String education,
        @NotBlank String experience,
        String certifications,
        String projects,
        String languages,
        String hobbies,
        String template
) {
}
