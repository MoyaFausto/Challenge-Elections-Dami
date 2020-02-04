package net.avalith.elections.service;

import net.avalith.elections.Utils.Utilities;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionCandidate;
import net.avalith.elections.model.ElectionHistory;
import net.avalith.elections.model.User;
import net.avalith.elections.model.Vote;
import net.avalith.elections.repository.ElectionHistoryRepository;
import org.apache.tomcat.jni.Local;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestElectionHistoryService {

    @Autowired
    ElectionHistoryService electionHistoryService;

    @Autowired
    @MockBean
    ElectionHistoryRepository electionHistoryRepository;

    @Autowired
    @MockBean
    ElectionService electionService;

    @Autowired
    @MockBean
    Utilities utilities;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveTest(){
        Election election = Election.builder()
                .id(1)
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .endDate(LocalDateTime.of(2020,2,3,14,0,0))
                .build();

        Candidate candidate = Candidate.builder()
                .id(2)
                .name("Damian")
                .lastname("Tacconi")
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionHistory electionHistory = ElectionHistory.builder()
                .date(Timestamp.from(Instant.now()))
                .percentage((float)0)
                .election(election)
                .candidate(candidate)
                .votes(0)
                .build();


        electionHistoryService.save(electionHistory);
        Mockito.verify(electionHistoryRepository,Mockito.times(1)).save(electionHistory);
    }

    @Test
    public void generateHistoryTest() {

        User user = User.builder()
                .name("Martin")
                .lastname("Diaz")
                .dni(323123)
                .id("02bf97b2-778d-42ab-b436-5c1bde314152")
                .isFake(0)
                .votes(new ArrayList<>())
                .build();

        Election election = Election.builder()
                .id(1)
                .startDate(LocalDateTime.of(2020, 1, 1, 13, 0, 0))
                .endDate(LocalDateTime.of(2021, 1, 1, 13, 0, 0))
                .electionCandidates(new ArrayList<>())
                .electionHistories(new ArrayList<>())
                .build();

        Candidate candidate = Candidate.builder()
                .id(2)
                .name("Damian")
                .lastname("Tacconi")
                .electionHistories(new ArrayList<>())
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .id(3)
                .election(election)
                .candidate(candidate)
                .votes(new ArrayList<>())
                .build();

        election.getElectionCandidates().add(electionCandidate);
        candidate.getElectionCandidates().add(electionCandidate);

        Vote vote = Vote.builder()
                .user(user)
                .id(5)
                .electionCandidate(electionCandidate)
                .build();

        electionCandidate.getVotes().add(vote);
        user.getVotes().add(vote);

        List<Election> activeElections = Collections.singletonList(election);

        Timestamp timestamp = Timestamp.valueOf("2020-03-01 09:01:15");

        ElectionHistory electionHistory = ElectionHistory.builder()
                .votes(1)
                .percentage((float)100)
                .candidate(candidate)
                .election(election)
                .date(timestamp)
                .build();

        Mockito.when(electionService.getActiveElections()).thenReturn(activeElections);
        Mockito.when(electionHistoryRepository.save(Mockito.any(ElectionHistory.class))).thenReturn(electionHistory);
        Mockito.when(utilities.now()).thenReturn(timestamp);

        electionHistoryService.generateHistory();
        Mockito.verify(electionHistoryRepository,Mockito.times(1)).save(electionHistory);
    }
}
