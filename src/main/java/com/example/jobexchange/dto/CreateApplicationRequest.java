package com.example.jobexchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateApplicationRequest(
        @NotNull Long jobId,
        @NotNull Long candidateId,
        Long cvId,
        @NotBlank String coverLetter
) {
}
