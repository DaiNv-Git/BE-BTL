package com.example.jobexchange.dto;

import com.example.jobexchange.domain.ApplicationStatus;
import com.example.jobexchange.domain.JobApplication;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long jobId,
        String jobTitle,
        String candidateName,
        String candidateEmail,
        String candidatePhone,
        String candidateHeadline,
        String coverLetter,
        ApplicationStatus status,
        LocalDateTime submittedAt
) {
    public static ApplicationResponse from(JobApplication application) {
        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getCandidate().getFullName(),
                application.getCandidate().getEmail(),
                application.getCandidate().getPhone(),
                application.getCandidate().getHeadline(),
                application.getCoverLetter(),
                application.getStatus(),
                application.getSubmittedAt()
        );
    }
}
