package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.entities.MessageResponse;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionCandidate;
import net.avalith.elections.model.User;
import net.avalith.elections.model.Vote;
import net.avalith.elections.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
public class VoteService {

    private final Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ElectionCandidateService electionCandidateService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ElectionService electionService;

    public MessageResponse save(Integer electionId, Integer candidateId, String userId){

        User user = this.userService.findById(userId);
        Optional<Vote> vote = user.getVotes()
                .stream()
                .filter(v -> v.getElectionCandidate().getElection().getId().equals(electionId))
                .findFirst();

        if(vote.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_HAS_ALREADY_VOTED);

        ElectionCandidate electionCandidate = this.electionCandidateService.getByCandidateAndElection(electionId,candidateId);

        this.voteRepository.save(Vote
                .builder()
                .user(user)
                .electionCandidate(electionCandidate)
                .build());

        return new MessageResponse("Voto ingresado con exito");
    }

    public Vote findById(Integer id){

        return this.voteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND));
    }

    public void delete(Integer id)
    {

        Vote electionCandidate = this.findById(id);
        this.voteRepository.delete(electionCandidate);
    }

    public void update(Vote newVote, Integer id){

        Vote oldVote = this.findById(id);
        oldVote.setElectionCandidate(newVote.getElectionCandidate());
        oldVote.setUser(newVote.getUser());

        this.voteRepository.save(oldVote);
    }

}
