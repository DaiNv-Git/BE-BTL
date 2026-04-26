package com.example.jobexchange.repository;

import com.example.jobexchange.domain.CandidateCv;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateCvRepository extends JpaRepository<CandidateCv, Long> {
    @Override
    @EntityGraph(attributePaths = "candidate")
    Optional<CandidateCv> findById(Long id);

    @EntityGraph(attributePaths = "candidate")
    Optional<CandidateCv> findByCandidateId(Long candidateId);

    @EntityGraph(attributePaths = "candidate")
    Optional<CandidateCv> findFirstByCandidateIdOrderByUpdatedAtDesc(Long candidateId);

    @EntityGraph(attributePaths = "candidate")
    List<CandidateCv> findByCandidateIdOrderByUpdatedAtDesc(Long candidateId);
}
