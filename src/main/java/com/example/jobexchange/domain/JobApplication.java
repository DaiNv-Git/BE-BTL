package com.example.jobexchange.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPost job;

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser candidate;

    @Column(nullable = false, length = 2000)
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @Column(nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    protected JobApplication() {
    }

    public JobApplication(JobPost job, AppUser candidate, String coverLetter) {
        this.job = job;
        this.candidate = candidate;
        this.coverLetter = coverLetter;
    }

    public Long getId() {
        return id;
    }

    public JobPost getJob() {
        return job;
    }

    public AppUser getCandidate() {
        return candidate;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void updateStatus(ApplicationStatus status) {
        this.status = status;
    }
}
