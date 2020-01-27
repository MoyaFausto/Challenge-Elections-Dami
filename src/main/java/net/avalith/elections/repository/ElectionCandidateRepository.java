package net.avalith.elections.repository;

import net.avalith.elections.interfaces.CandidateView;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectionCandidateRepository extends JpaRepository<ElectionCandidate, Integer> {
    String queryGetAllCandidates = "select c.name, c.lastname from election_candidate ec inner join candidates c on c.id = ec.candidate_id  where election_id = :id";
    String getByCandidateAndElection="select * from election_candidate where candidate_id=:cid and election_id=:eid";

    @Query(value = queryGetAllCandidates , nativeQuery = true)
    List<CandidateView> getCandidatesByElectionId(@Param("id") Integer id);

    @Query(value = getByCandidateAndElection, nativeQuery = true)
    ElectionCandidate getByCandidateAndElection(@Param("cid") Integer candidateId, @Param("eid") Integer electionId);
}
