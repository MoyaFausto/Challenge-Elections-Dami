package net.avalith.elections.repository;

import net.avalith.elections.model.UserByElectionCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserByElectionCandidateRepository extends JpaRepository<UserByElectionCandidate, Integer> {
}
