package com.example.jobexchange.controller;

import com.example.jobexchange.config.AuthContext;
import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.ApplicationStatus;
import com.example.jobexchange.domain.CandidateCv;
import com.example.jobexchange.domain.JobApplication;
import com.example.jobexchange.domain.JobPost;
import com.example.jobexchange.domain.JobStatus;
import com.example.jobexchange.domain.UserRole;
import com.example.jobexchange.domain.UserNotification;
import com.example.jobexchange.dto.ApplicationResponse;
import com.example.jobexchange.dto.CreateApplicationRequest;
import com.example.jobexchange.repository.AppUserRepository;
import com.example.jobexchange.repository.CandidateCvRepository;
import com.example.jobexchange.repository.JobApplicationRepository;
import com.example.jobexchange.repository.JobPostRepository;
import com.example.jobexchange.repository.UserNotificationRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/applications")
@org.springframework.transaction.annotation.Transactional
public class ApplicationController {
    private final JobApplicationRepository applications;
    private final JobPostRepository jobs;
    private final AppUserRepository users;
    private final CandidateCvRepository cvs;
    private final UserNotificationRepository notifications;
    private final AuthContext authContext;

    public ApplicationController(JobApplicationRepository applications, JobPostRepository jobs, AppUserRepository users, CandidateCvRepository cvs, UserNotificationRepository notifications, AuthContext authContext) {
        this.applications = applications;
        this.jobs = jobs;
        this.users = users;
        this.cvs = cvs;
        this.notifications = notifications;
        this.authContext = authContext;
    }

    @GetMapping
    public List<ApplicationResponse> list(@RequestParam(required = false) Long jobId, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        AppUser currentUser = authContext.requireUser(userId);
        if (currentUser.getRole() == UserRole.JOB_SEEKER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only employer or admin can view applications");
        }
        return (jobId == null ? applications.findAll() : applications.findByJobIdOrderBySubmittedAtDesc(jobId))
                .stream()
                .filter(application -> currentUser.getRole() == UserRole.ADMIN
                        || application.getJob().getEmployer().getId().equals(currentUser.getId()))
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse create(@Valid @RequestBody CreateApplicationRequest request, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        AppUser candidate = authContext.requireRole(userId, UserRole.JOB_SEEKER);
        JobPost job = jobs.findById(request.jobId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job not found"));
        if (job.getStatus() != JobStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only approved jobs can receive applications");
        }
        
        CandidateCv selectedCv = request.cvId() == null
                ? cvs.findFirstByCandidateIdOrderByUpdatedAtDesc(candidate.getId()).orElse(null)
                : cvs.findById(request.cvId())
                        .filter(cv -> cv.getCandidate().getId().equals(candidate.getId()))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected CV not found"));
        JobApplication application = applications.save(new JobApplication(job, candidate, request.coverLetter(), selectedCv));
        
        notifications.save(new UserNotification(
                job.getEmployer(),
                "Co ung vien moi",
                candidate.getFullName() + " vua ung tuyen vao vi tri " + job.getTitle() + ".",
                job.getId()
        ));
        
        return toResponse(application);
    }

    @RequestMapping(path = "/{id}/accept", method = {RequestMethod.PATCH, RequestMethod.POST})
    public ApplicationResponse accept(@PathVariable Long id, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return updateStatus(id, userId, ApplicationStatus.ACCEPTED);
    }

    @RequestMapping(path = "/{id}/reject", method = {RequestMethod.PATCH, RequestMethod.POST})
    public ApplicationResponse reject(@PathVariable Long id, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return updateStatus(id, userId, ApplicationStatus.REJECTED);
    }

    private ApplicationResponse updateStatus(Long id, Long userId, ApplicationStatus status) {
        AppUser currentUser = authContext.requireRole(userId, UserRole.EMPLOYER);
        JobApplication application = applications.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        if (!application.getJob().getEmployer().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot manage applications of another employer");
        }

        String companyName = application.getJob().getCompanyName();
        String jobTitle = application.getJob().getTitle();
        AppUser candidate = application.getCandidate();
        application.updateStatus(status);
        JobApplication saved = applications.save(application);
        String title = status == ApplicationStatus.ACCEPTED ? "Ho so da duoc chap nhan" : "Ho so chua phu hop";
        String message = status == ApplicationStatus.ACCEPTED
                ? "Nha tuyen dung " + companyName + " da chap nhan ho so ung tuyen vi tri " + jobTitle + ". Vui long theo doi email/dien thoai de nhan lich phong van."
                : "Nha tuyen dung " + companyName + " da tu choi ho so ung tuyen vi tri " + jobTitle + ". Ban co the tiep tuc ung tuyen cac viec lam phu hop khac.";
        notifications.save(new UserNotification(candidate, title, message, application.getJob().getId()));
        return applications.findById(saved.getId())
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Application saved but cannot be loaded"));
    }

    private ApplicationResponse toResponse(JobApplication application) {
        CandidateCv cv = application.getCvId() == null
                ? cvs.findFirstByCandidateIdOrderByUpdatedAtDesc(application.getCandidate().getId()).orElse(null)
                : cvs.findById(application.getCvId()).orElse(null);
        return ApplicationResponse.from(application, cv);
    }
}
