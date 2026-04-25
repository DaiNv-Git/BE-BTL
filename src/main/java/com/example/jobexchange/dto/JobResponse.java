package com.example.jobexchange.dto;

import com.example.jobexchange.domain.JobPost;
import com.example.jobexchange.domain.JobStatus;
import java.time.LocalDateTime;

public record JobResponse(
        Long id,
        String title,
        String companyName,
        String location,
        String salaryRange,
        String description,
        String requirements,
        JobStatus status,
        LocalDateTime createdAt,
        Long employerId,
        String employerName
) {
    public static JobResponse from(JobPost job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompanyName(),
                job.getLocation(),
                job.getSalaryRange(),
                job.getDescription(),
                job.getRequirements(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getEmployer().getId(),
                job.getEmployer().getFullName()
        );
    }
}
