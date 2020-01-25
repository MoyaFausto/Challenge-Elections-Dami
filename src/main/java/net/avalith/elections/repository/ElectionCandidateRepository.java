package net.avalith.elections.repository;

import net.avalith.elections.model.ElectionCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionCandidateRepository extends JpaRepository<ElectionCandidate, Integer> {
}
