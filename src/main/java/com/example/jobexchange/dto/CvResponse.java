package com.example.jobexchange.dto;

import com.example.jobexchange.domain.CandidateCv;
import java.time.LocalDateTime;

public record CvResponse(
        Long id,
        Long candidateId,
        String candidateName,
        String candidateEmail,
        String candidatePhone,
        String title,
        String desiredPosition,
        String experienceLevel,
        String summary,
        String skills,
        String education,
        String experience,
        String certifications,
        String projects,
        String languages,
        String hobbies,
        String template,
        String cvData,
        LocalDateTime updatedAt
) {
    public static CvResponse from(CandidateCv cv) {
        return new CvResponse(
                cv.getId(),
                cv.getCandidate().getId(),
                cv.getCandidate().getFullName(),
                cv.getCandidate().getEmail(),
                cv.getCandidate().getPhone(),
                cv.getTitle(),
                cv.getDesiredPosition(),
                cv.getExperienceLevel(),
                cv.getSummary(),
                cv.getSkills(),
                cv.getEducation(),
                cv.getExperience(),
                cv.getCertifications(),
                cv.getProjects(),
                cv.getLanguages(),
                cv.getHobbies(),
                cv.getTemplate(),
                cv.getCvData(),
                cv.getUpdatedAt()
        );
    }
}
