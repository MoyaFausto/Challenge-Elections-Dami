package net.avalith.elections.service;

import net.avalith.elections.entities.CandidateVotes;
import net.avalith.elections.entities.ElectionListResponse;
import net.avalith.elections.entities.ElectionResponse;
import net.avalith.elections.entities.ElectionVotes;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionHistory;
import net.avalith.elections.repository.ElectionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Service
public class ElectionHistoryService {

    @Autowired
    private ElectionHistoryRepository electionHistoryRepository;

    @Autowired
    private VoteService voteService;

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateService candidateService;

    public void save(ElectionHistory electionHistory){
        this.electionHistoryRepository.save(electionHistory);
    }

    public void generateHistory(ElectionListResponse electionListResponse){

        List<ElectionVotes> electionVotes = electionListResponse.getElections().stream()
                .filter(e -> e.getEndDate().isAfter(LocalDateTime.now()))
                .map(e -> this.voteService.getElectionResult(e.getId()))
                .collect(Collectors.toList());

        electionVotes.forEach(ev -> {

            CandidateVotes candidateVotes = ev.getCandidates().stream()
                    .max(Comparator.comparing(CandidateVotes::getVotes))
                    .orElseThrow(NoSuchElementException::new);

            Candidate candidate = this.candidateService.findById(candidateVotes.getId_candidate());
            Election election = this.electionService.findById(ev.getId_election());

            ElectionHistory electionHistory = ElectionHistory.builder()
                    .election(election)
                    .candidate(candidate)
                    .percentage(((float) candidateVotes.getVotes() / (float)ev.getTotal_votes() * 100))
                    .votes(candidateVotes.getVotes())
                    .date(Timestamp.from(Instant.now()))
                    .build();

            this.save(electionHistory);
        });
    }
}
