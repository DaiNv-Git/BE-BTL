package com.example.jobexchange.repository;

import com.example.jobexchange.domain.CandidateCv;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateCvRepository extends JpaRepository<CandidateCv, Long> {
    @EntityGraph(attributePaths = "candidate")
    Optional<CandidateCv> findByCandidateId(Long candidateId);
}
