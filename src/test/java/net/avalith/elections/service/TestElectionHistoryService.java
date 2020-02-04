package net.avalith.elections.service;

import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionHistory;
import net.avalith.elections.repository.ElectionHistoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestElectionHistoryService {

    @Autowired
    ElectionHistoryService electionHistoryService;

    @Autowired
    @MockBean
    ElectionHistoryRepository electionHistoryRepository;

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

    }
}
