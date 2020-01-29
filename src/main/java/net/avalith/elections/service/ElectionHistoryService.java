package net.avalith.elections.service;

import net.avalith.elections.entities.CandidateVotes;
import net.avalith.elections.entities.ElectionListResponse;
import net.avalith.elections.entities.ElectionResponse;
import net.avalith.elections.entities.ElectionVotes;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionCandidate;
import net.avalith.elections.model.ElectionHistory;
import net.avalith.elections.model.Vote;
import net.avalith.elections.repository.ElectionHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(ElectionHistoryService.class);

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

    public void generateHistory(){

        List<Election> elections = this.electionService.getActiveElections();

        elections.forEach(e -> {

            ElectionCandidate electionCandidate = e.getElectionCandidates().stream()
                        .max(Comparator.comparing(ec -> ec.getVotes().size()))
                        .orElseThrow(NoSuchElementException::new);

            int totalVotes = e.getElectionCandidates().stream()
                    .mapToInt(ec -> ec.getVotes().size())
                    .sum();

            Integer candidateVotes = electionCandidate.getVotes().size();

            this.save(ElectionHistory.builder()
                    .candidate(electionCandidate.getCandidate())
                    .votes(candidateVotes)
                    .election(e)
                    .percentage((float)candidateVotes / (float)totalVotes * 100)
                    .date(Timestamp.from(Instant.now()))
                    .build());
        });
    }
}
