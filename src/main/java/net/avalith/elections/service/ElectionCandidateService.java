package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.entities.CandidatesOfAnElectionResult;
import net.avalith.elections.model.ElectionCandidate;
import net.avalith.elections.repository.ElectionCandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ElectionCandidateService {

    @Autowired
    private ElectionCandidateRepository electionCandidateRepository;

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateService candidateService;

    public Integer save(ElectionCandidate electionCandidate){

        return this.electionCandidateRepository.save(electionCandidate).getId();
    }

    public ElectionCandidate findById(Integer id){

        return this.electionCandidateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND));
    }

    public void delete(Integer id)
    {

        ElectionCandidate electionCandidate = this.findById(id);
        this.electionCandidateRepository.delete(electionCandidate);
    }

    public void update(ElectionCandidate newElectionCandidate, Integer id){

        ElectionCandidate oldElectionCandidate = this.findById(id);
        oldElectionCandidate.setCandidate(newElectionCandidate.getCandidate());
        oldElectionCandidate.setElection(newElectionCandidate.getElection());

        this.electionCandidateRepository.save(oldElectionCandidate);
    }

    public CandidatesOfAnElectionResult getAllCandidates(Integer id){

        return new CandidatesOfAnElectionResult(this.electionCandidateRepository.getCandidatesByElectionId(id));
    }

    public ElectionCandidate getByCandidateAndElection(Integer electionId, Integer candidateId){

        return this.electionCandidateRepository.getByCandidateAndElection(candidateId,electionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.ELECTION_CANDIDATE_NOT_FOUND)
        );
    }

}
