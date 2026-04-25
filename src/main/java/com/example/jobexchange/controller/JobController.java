package com.example.jobexchange.controller;

import com.example.jobexchange.config.AuthContext;
import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.JobPost;
import com.example.jobexchange.domain.JobStatus;
import com.example.jobexchange.domain.UserRole;
import com.example.jobexchange.dto.CreateJobRequest;
import com.example.jobexchange.dto.JobResponse;
import com.example.jobexchange.repository.AppUserRepository;
import com.example.jobexchange.repository.JobApplicationRepository;
import com.example.jobexchange.repository.JobPostRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobPostRepository jobs;
    private final AppUserRepository users;
    private final JobApplicationRepository applications;
    private final AuthContext authContext;

    public JobController(JobPostRepository jobs, AppUserRepository users, JobApplicationRepository applications, AuthContext authContext) {
        this.jobs = jobs;
        this.users = users;
        this.applications = applications;
        this.authContext = authContext;
    }

    @GetMapping
    public List<JobResponse> list(@RequestParam(required = false) JobStatus status) {
        return (status == null ? jobs.findAllByOrderByCreatedAtDesc() : jobs.findByStatusOrderByCreatedAtDesc(status))
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public JobResponse detail(@PathVariable Long id) {
        return jobs.findById(id)
                .map(JobResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse create(@Valid @RequestBody CreateJobRequest request, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        AppUser employer = authContext.requireRole(userId, UserRole.EMPLOYER);
        JobPost job = new JobPost(
                request.title(),
                request.companyName(),
                request.location(),
                request.salaryRange(),
                request.description(),
                request.requirements(),
                employer
        );
        return JobResponse.from(jobs.save(job));
    }

    @RequestMapping(path = "/{id}/approve", method = {RequestMethod.PATCH, RequestMethod.POST})
    public JobResponse approve(@PathVariable Long id, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        authContext.requireRole(userId, UserRole.ADMIN);
        JobPost job = jobs.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        job.approve();
        return JobResponse.from(jobs.save(job));
    }

    @RequestMapping(path = "/{id}/close", method = {RequestMethod.PATCH, RequestMethod.POST})
    public JobResponse close(@PathVariable Long id, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        authContext.requireRole(userId, UserRole.ADMIN);
        JobPost job = jobs.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        job.close();
        return JobResponse.from(jobs.save(job));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        authContext.requireRole(userId, UserRole.ADMIN);
        if (!jobs.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
        }
        applications.deleteByJobId(id);
        jobs.deleteById(id);
    }
}
