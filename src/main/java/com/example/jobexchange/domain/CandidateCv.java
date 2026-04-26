package com.example.jobexchange.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_cvs")
public class CandidateCv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser candidate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String desiredPosition;

    @Column(nullable = false)
    private String experienceLevel;

    @Column(nullable = false, length = 2000)
    private String summary;

    @Column(nullable = false, length = 2000)
    private String skills;

    @Column(nullable = false, length = 2000)
    private String education;

    @Column(nullable = false, length = 2000)
    private String experience;

    @Column(nullable = false, length = 1000)
    private String certifications;

    @Column(length = 2000)
    private String projects;

    @Column(length = 500)
    private String languages;

    @Column(length = 500)
    private String hobbies;

    @Column(nullable = true)
    private String template = "classic";

    @Column(columnDefinition = "LONGTEXT")
    private String cvData;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected CandidateCv() {
    }

    public CandidateCv(AppUser candidate, String title, String desiredPosition, String experienceLevel, String summary, String skills, String education, String experience, String certifications, String projects, String languages, String hobbies, String template, String cvData) {
        this.candidate = candidate;
        update(title, desiredPosition, experienceLevel, summary, skills, education, experience, certifications, projects, languages, hobbies, template, cvData);
    }

    public void update(String title, String desiredPosition, String experienceLevel, String summary, String skills, String education, String experience, String certifications, String projects, String languages, String hobbies, String template, String cvData) {
        this.title = title;
        this.desiredPosition = desiredPosition;
        this.experienceLevel = experienceLevel;
        this.summary = summary;
        this.skills = skills;
        this.education = education;
        this.experience = experience;
        this.certifications = certifications;
        this.projects = projects;
        this.languages = languages;
        this.hobbies = hobbies;
        this.template = template == null || template.isBlank() ? "classic" : template;
        this.cvData = cvData;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public AppUser getCandidate() {
        return candidate;
    }

    public String getTitle() {
        return title;
    }

    public String getDesiredPosition() {
        return desiredPosition;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public String getSummary() {
        return summary;
    }

    public String getSkills() {
        return skills;
    }

    public String getEducation() {
        return education;
    }

    public String getExperience() {
        return experience;
    }

    public String getCertifications() {
        return certifications;
    }

    public String getTemplate() {
        return template;
    }

    public String getProjects() {
        return projects;
    }

    public String getLanguages() {
        return languages;
    }

    public String getHobbies() {
        return hobbies;
    }

    public String getCvData() {
        return cvData;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
