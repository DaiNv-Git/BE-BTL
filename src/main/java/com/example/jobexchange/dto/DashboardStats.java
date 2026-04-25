package com.example.jobexchange.dto;

public record DashboardStats(
        long totalJobs,
        long approvedJobs,
        long pendingJobs,
        long totalApplications,
        long totalCandidates,
        long totalEmployers
) {
}
