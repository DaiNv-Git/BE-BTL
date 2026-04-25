package com.example.jobexchange.repository;

import com.example.jobexchange.domain.JobApplication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    @EntityGraph(attributePaths = {"job", "job.employer", "candidate"})
    List<JobApplication> findByJobIdOrderBySubmittedAtDesc(Long jobId);

    @Override
    @EntityGraph(attributePaths = {"job", "job.employer", "candidate"})
    Optional<JobApplication> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"job", "job.employer", "candidate"})
    List<JobApplication> findAll();

    @Transactional
    long deleteByJobId(Long jobId);
}
