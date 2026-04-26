package com.example.jobexchange.controller;

import com.example.jobexchange.config.AuthContext;
import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.CandidateCv;
import com.example.jobexchange.domain.UserRole;
import com.example.jobexchange.dto.CvResponse;
import com.example.jobexchange.dto.SaveCvRequest;
import com.example.jobexchange.repository.CandidateCvRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        return cvs.findFirstByCandidateIdOrderByUpdatedAtDesc(candidate.getId())
                .map(CvResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CV not found"));
    }

    @GetMapping("/me/list")
    public List<CvResponse> myCvs(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        AppUser candidate = authContext.requireRole(userId, UserRole.JOB_SEEKER);
        return cvs.findByCandidateIdOrderByUpdatedAtDesc(candidate.getId()).stream()
                .map(CvResponse::from)
                .toList();
    }

    @PutMapping("/me")
    public CvResponse saveMyCv(@RequestHeader(value = "X-User-Id", required = false) Long userId, @Valid @RequestBody SaveCvRequest request) {
        AppUser candidate = authContext.requireRole(userId, UserRole.JOB_SEEKER);
        CandidateCv cv = cvs.findFirstByCandidateIdOrderByUpdatedAtDesc(candidate.getId())
                .orElseGet(() -> newCv(candidate, request));
        updateCv(cv, request);
        return CvResponse.from(cvs.save(cv));
    }

    @PostMapping("/me")
    public CvResponse createMyCv(@RequestHeader(value = "X-User-Id", required = false) Long userId, @Valid @RequestBody SaveCvRequest request) {
        AppUser candidate = authContext.requireRole(userId, UserRole.JOB_SEEKER);
        return CvResponse.from(cvs.save(newCv(candidate, request)));
    }

    @PutMapping("/me/{id}")
    public CvResponse updateMyCv(@PathVariable Long id, @RequestHeader(value = "X-User-Id", required = false) Long userId, @Valid @RequestBody SaveCvRequest request) {
        AppUser candidate = authContext.requireRole(userId, UserRole.JOB_SEEKER);
        CandidateCv cv = cvs.findById(id)
                .filter(item -> item.getCandidate().getId().equals(candidate.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CV not found"));
        updateCv(cv, request);
        return CvResponse.from(cvs.save(cv));
    }

    @DeleteMapping("/me/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyCv(@PathVariable Long id, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        AppUser candidate = authContext.requireRole(userId, UserRole.JOB_SEEKER);
        CandidateCv cv = cvs.findById(id)
                .filter(item -> item.getCandidate().getId().equals(candidate.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CV not found"));
        cvs.delete(cv);
    }

    private CandidateCv newCv(AppUser candidate, SaveCvRequest request) {
        return new CandidateCv(
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
                request.template(),
                request.cvData()
        );
    }

    private void updateCv(CandidateCv cv, SaveCvRequest request) {
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
                request.template(),
                request.cvData()
        );
    }
}
