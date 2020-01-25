package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.entities.ElectionListResponse;
import net.avalith.elections.entities.ElectionRequest;
import net.avalith.elections.entities.ElectionResponse;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionCandidate;
import net.avalith.elections.model.UserByElectionCandidate;
import net.avalith.elections.repository.ElectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectionService {

    private final Logger logger = LoggerFactory.getLogger(ElectionService.class);

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ElectionCandidateService electionCandidateService;

    @Autowired
    private UserByElectionCandidateService userByElectionCandidateService;

    public ElectionResponse save(ElectionRequest electionRequest){


        LocalDateTime startDate = electionRequest.getStartDate();
        LocalDateTime endDate = electionRequest.getEndDate();


        //checking valid dates
        if(endDate.isBefore(startDate) || endDate.isEqual(startDate))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ErrorMessage.START_DATE_IS_GREATER_THAN_OR_EQUAL_TO_END_DATE);


        //get candidate entities by id
        List<Candidate> candidates = electionRequest.getCandidateIds().stream()
                                        .map(c -> this.candidateService.findById(c))
                                        .collect(Collectors.toList());


        //Creating a new empty election and then insert it the dates.
        Election election = new Election();
        election.setElectionCandidates(new ArrayList<>());
        election.setStartDate(startDate);
        election.setEndDate(endDate);

        //save the election
        election = this.electionRepository.save(election);

        //Loop candidates and create a new ElectionCandidate
        for(Candidate candidate : candidates){
            ElectionCandidate electionCandidate = new ElectionCandidate();
            electionCandidate.setCandidate(candidate);
            electionCandidate.setElection(election);
            electionCandidate.setId(this.electionCandidateService.save(electionCandidate));

            //update election with the new Election Candidate

            //i need to get the election from repository because otherwise hibernate will add the election multiple times.
            election = this.findById(election.getId());

            election.getElectionCandidates().add(electionCandidate);
            this.electionRepository.save(election);

            //update candidate with the new Election Candidate
            candidate.getElectionCandidates().add(electionCandidate);
            this.candidateService.save(candidate);
        }

        return new ElectionResponse(election.getId());
    }

    public Election findById(Integer id){

        return this.electionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.ELECTION_NOT_FOUND));
    }

    public void delete(Integer id){

        Election election = this.findById(id);
        this.electionRepository.delete(election);
    }

    public void update(Election newElection,  Integer id){

        Election oldElection = this.findById(id);
        oldElection.setStartDate(newElection.getStartDate());
        oldElection.setEndDate(newElection.getEndDate());
        oldElection.setElectionCandidates(newElection.getElectionCandidates());

        this.electionRepository.save(oldElection);

    }
    public ElectionListResponse findAll(){
        return new ElectionListResponse(this.electionRepository.findAll());
    }
}
