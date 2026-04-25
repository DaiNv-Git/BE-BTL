package com.example.jobexchange.controller;

import com.example.jobexchange.domain.JobStatus;
import com.example.jobexchange.domain.UserRole;
import com.example.jobexchange.dto.DashboardStats;
import com.example.jobexchange.repository.AppUserRepository;
import com.example.jobexchange.repository.JobApplicationRepository;
import com.example.jobexchange.repository.JobPostRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final JobPostRepository jobs;
    private final JobApplicationRepository applications;
    private final AppUserRepository users;

    public DashboardController(JobPostRepository jobs, JobApplicationRepository applications, AppUserRepository users) {
        this.jobs = jobs;
        this.applications = applications;
        this.users = users;
    }

    @GetMapping("/stats")
    public DashboardStats stats() {
        return new DashboardStats(
                jobs.count(),
                jobs.findByStatusOrderByCreatedAtDesc(JobStatus.APPROVED).size(),
                jobs.findByStatusOrderByCreatedAtDesc(JobStatus.PENDING).size(),
                applications.count(),
                users.findByRole(UserRole.JOB_SEEKER).size(),
                users.findByRole(UserRole.EMPLOYER).size()
        );
    }
}
