package com.example.jobexchange.repository;

import com.example.jobexchange.domain.JobPost;
import com.example.jobexchange.domain.JobStatus;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    @EntityGraph(attributePaths = "employer")
    List<JobPost> findByStatusOrderByCreatedAtDesc(JobStatus status);

    @EntityGraph(attributePaths = "employer")
    List<JobPost> findAllByOrderByCreatedAtDesc();
}
