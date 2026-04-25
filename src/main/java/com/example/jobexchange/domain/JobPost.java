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
@Table(name = "job_posts")
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String salaryRange;

    @Column(nullable = false, length = 3000)
    private String description;

    @Column(nullable = false, length = 2000)
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser employer;

    protected JobPost() {
    }

    public JobPost(String title, String companyName, String location, String salaryRange, String description, String requirements, AppUser employer) {
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.salaryRange = salaryRange;
        this.description = description;
        this.requirements = requirements;
        this.employer = employer;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public JobStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AppUser getEmployer() {
        return employer;
    }

    public void approve() {
        this.status = JobStatus.APPROVED;
    }

    public void close() {
        this.status = JobStatus.CLOSED;
    }
}
