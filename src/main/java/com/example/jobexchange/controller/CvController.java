package com.example.jobexchange.controller;

import com.example.jobexchange.config.AuthContext;
import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.CandidateCv;
import com.example.jobexchange.domain.UserRole;
import com.example.jobexchange.dto.CvResponse;
import com.example.jobexchange.dto.SaveCvRequest;
import com.example.jobexchange.repository.CandidateCvRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cvs")
public class CvController {
    private final CandidateCvRepository cvs;
    private final AuthContext authContext;

    public CvController(CandidateCvRepository cvs, AuthContext authContext) {
        this.cvs = cvs;
        this.authContext = authContext;
    }

    @GetMapping("/me")
    public CvResponse myCv(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        AppUser candidate = authContext.requireRole(userId, UserRole.JOB_SEEKER);
        return cvs.findByCandidateId(candidate.getId())
                .map(CvResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CV not found"));
    }

    @PutMapping("/me")
    public CvResponse saveMyCv(@RequestHeader(value = "X-User-Id", required = false) Long userId, @Valid @RequestBody SaveCvRequest request) {
        AppUser candidate = authContext.requireRole(userId, UserRole.JOB_SEEKER);
        CandidateCv cv = cvs.findByCandidateId(candidate.getId())
                .orElseGet(() -> new CandidateCv(
                        candidate,
                        request.title(),
                        request.desiredPosition(),
                        request.experienceLevel(),
                        request.summary(),
                        request.skills(),
                        request.education(),
                        request.experience(),
                        request.certifications() == null ? "" : request.certifications(),
                        request.projects() == null ? "" : request.projects(),
                        request.languages() == null ? "" : request.languages(),
                        request.hobbies() == null ? "" : request.hobbies(),
                        request.template()
                ));
        cv.update(
                request.title(),
                request.desiredPosition(),
                request.experienceLevel(),
                request.summary(),
                request.skills(),
                request.education(),
                request.experience(),
                request.certifications() == null ? "" : request.certifications(),
                request.projects() == null ? "" : request.projects(),
                request.languages() == null ? "" : request.languages(),
                request.hobbies() == null ? "" : request.hobbies(),
                request.template()
        );
        CandidateCv saved = cvs.save(cv);
        return cvs.findByCandidateId(saved.getCandidate().getId())
                .map(CvResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CV saved but cannot be loaded"));
    }
}
