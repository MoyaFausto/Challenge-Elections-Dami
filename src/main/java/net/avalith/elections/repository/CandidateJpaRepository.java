package net.avalith.elections.repository;

import net.avalith.elections.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateJpaRepository extends JpaRepository<Candidate, Integer> {
}
