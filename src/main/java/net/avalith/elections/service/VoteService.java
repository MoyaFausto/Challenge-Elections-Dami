package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.entities.CandidateVotes;
import net.avalith.elections.entities.ElectionVotes;
import net.avalith.elections.entities.FakeUsers;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if(user.getVotes()
                .stream()
                .anyMatch(v -> v.getElectionCandidate().getElection().getId().equals(electionId)))
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

    private CandidateVotes createCandidateVotes(ElectionCandidate ec){
        Candidate candidate = ec.getCandidate();
        Integer quantityVotes = ec.getVotes().size();
        return CandidateVotes.builder()
                .first_name(candidate.getName())
                .last_name(candidate.getLastname())
                .id_candidate(candidate.getId())
                .votes(quantityVotes)
                .build();
    }

    public ElectionVotes getElectionResult(Integer id){

        Election election = this.electionService.findById(id);

        List<CandidateVotes> candidateVotes = election.getElectionCandidates().stream()
                .map(this::createCandidateVotes)
                .collect(Collectors.toList());

        Integer totalVotes = candidateVotes.stream()
                .map(CandidateVotes::getVotes)
                .reduce(0, Integer::sum);

        return ElectionVotes.builder()
                .id_election(id)
                .total_votes(totalVotes)
                .candidates(candidateVotes)
                .build();
    }

    public MessageResponse fakeVote(Integer electionId, Integer candidateId){

        List<User> fakeUsers = this.userService.findFakes();

        fakeUsers = fakeUsers.stream()
                .filter(u -> u.getVotes().stream().noneMatch(v -> v.getElectionCandidate().getElection().getId().equals(electionId)))
                .collect(Collectors.toList());

        if(fakeUsers.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,ErrorMessage.FAKE_USERS_CANNOT_VOTE);

        fakeUsers.forEach(fu -> this.save(electionId,candidateId,fu.getId()));

        return new MessageResponse("Votos generados correctamente");
    }

}
