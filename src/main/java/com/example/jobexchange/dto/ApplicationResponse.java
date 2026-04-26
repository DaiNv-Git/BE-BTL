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
        Long cvId,
        String cvTitle,
        String desiredPosition,
        String cvTemplate,
        String cvData,
        String coverLetter,
        ApplicationStatus status,
        LocalDateTime submittedAt
) {
    public static ApplicationResponse from(JobApplication application) {
        return from(application, null);
    }

    public static ApplicationResponse from(JobApplication application, com.example.jobexchange.domain.CandidateCv cv) {
        return new ApplicationResponse(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getCandidate().getFullName(),
                application.getCandidate().getEmail(),
                application.getCandidate().getPhone(),
                application.getCandidate().getHeadline(),
                application.getCvId() == null && cv != null ? cv.getId() : application.getCvId(),
                application.getCvTitle() == null && cv != null ? cv.getTitle() : application.getCvTitle(),
                application.getDesiredPosition() == null && cv != null ? cv.getDesiredPosition() : application.getDesiredPosition(),
                application.getCvTemplate() == null && cv != null ? cv.getTemplate() : application.getCvTemplate(),
                application.getCvData() == null && cv != null ? cv.getCvData() : application.getCvData(),
                application.getCoverLetter(),
                application.getStatus(),
                application.getSubmittedAt()
        );
    }
}
