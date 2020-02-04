package net.avalith.elections.service;

import net.avalith.elections.entities.CandidatesOfAnElectionResult;
import net.avalith.elections.interfaces.CandidateView;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionCandidate;
import net.avalith.elections.repository.ElectionCandidateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestElectionCandidateService {

    @Autowired
    ElectionCandidateService electionCandidateService;

    @Autowired
    @MockBean
    ElectionCandidateRepository electionCandidateRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = ResponseStatusException.class)
    public void findByIdNullTest(){
        Integer id = 1;

        when(electionCandidateRepository.findById(id)).thenReturn(Optional.empty());
        ElectionCandidate electionCandidateNull = electionCandidateService.findById(id);

        Assert.assertNull(electionCandidateNull);
    }

    @Test
    public void findByIdTest(){
        Integer electionId = 1;
        Integer candidateId = 2;
        Integer electionCandidateId = 3;

        Election election = Election.builder()
                .id(electionId)
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .endDate(LocalDateTime.of(2020,2,3,14,0,0))
                .build();

        Candidate candidate = Candidate.builder()
                .id(candidateId)
                .name("Damian")
                .lastname("Tacconi")
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidateExpected = ElectionCandidate.builder()
                .id(electionCandidateId)
                .election(election)
                .candidate(candidate)
                .votes(new ArrayList<>())
                .build();


        when(electionCandidateRepository.findById(electionCandidateId)).thenReturn(Optional.of(electionCandidateExpected));
        Assert.assertEquals(electionCandidateExpected, electionCandidateService.findById(electionCandidateId));
    }

    @Test
    public void saveTest(){
        Integer electionId = 1;
        Integer candidateId = 2;
        Integer electionCandidateId = 3;

        Election election = Election.builder()
                .id(electionId)
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .endDate(LocalDateTime.of(2020,2,3,14,0,0))
                .build();

        Candidate candidate = Candidate.builder()
                .id(candidateId)
                .name("Damian")
                .lastname("Tacconi")
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .election(election)
                .candidate(candidate)
                .votes(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidateExpected = ElectionCandidate.builder()
                .candidate(candidate)
                .election(election)
                .id(electionCandidateId)
                .votes(new ArrayList<>())
                .build();

        Mockito.when(electionCandidateRepository.save(electionCandidate)).thenReturn(electionCandidateExpected);

        Assert.assertEquals(electionCandidateExpected.getId() , electionCandidateService.save(electionCandidate));
        Mockito.verify(electionCandidateRepository,Mockito.times(1)).save(electionCandidate);
    }

    @Test
    public void deleteTest(){
        Integer id = 1;
        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .id(id)
                .build();

        when(electionCandidateRepository.findById(id)).thenReturn(Optional.ofNullable(electionCandidate));

        electionCandidateService.delete(id);
        verify(electionCandidateRepository, times(1)).delete(electionCandidate);
    }

    @Test
    public void updateTest(){
        Integer id = 1;

        Election election = Election.builder()
                .id(2)
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .endDate(LocalDateTime.of(2020,2,3,14,0,0))
                .build();

        Candidate oldCandidate = Candidate.builder()
                .id(3)
                .name("Martin")
                .lastname("Diaz")
                .electionCandidates(new ArrayList<>())
                .build();

        Candidate newCandidate = Candidate.builder()
                .id(3)
                .name("Damian")
                .lastname("Tacconi")
                .electionCandidates(new ArrayList<>())
                .build();


        ElectionCandidate newElectionCandidate = ElectionCandidate.builder()
                .election(election)
                .candidate(newCandidate)
                .votes(new ArrayList<>())
                .build();

        ElectionCandidate oldElectionCandidate = ElectionCandidate.builder()
                .id(id)
                .election(election)
                .candidate(oldCandidate)
                .votes(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidateUpdated = ElectionCandidate.builder()
                .id(id)
                .election(election)
                .candidate(newCandidate)
                .build();

        Mockito.when(electionCandidateRepository.findById(id)).thenReturn(Optional.ofNullable(oldElectionCandidate));
        assert oldElectionCandidate != null;
        oldElectionCandidate.setElection(newElectionCandidate.getElection());
        oldElectionCandidate.setCandidate(newElectionCandidate.getCandidate());
        oldElectionCandidate.setVotes(newElectionCandidate.getVotes());

        Mockito.when(electionCandidateRepository.save(oldElectionCandidate)).thenReturn(electionCandidateUpdated);

        electionCandidateService.update(newElectionCandidate,id);

        Mockito.verify(electionCandidateRepository,times(1)).findById(id);
        Mockito.verify(electionCandidateRepository,times(1)).save(oldElectionCandidate);
    }

    @Test
    public void getAllCandidatesTest(){
        Integer electionId = 1;
        List<CandidateView> candidateViewList = Collections.singletonList(new CandidateView() {
            @Override
            public String getName() {
                return "Damian";
            }

            @Override
            public String getLastname() {
                return "Tacconi";
            }
        });

        CandidatesOfAnElectionResult candidatesOfAnElectionResult = new CandidatesOfAnElectionResult(candidateViewList);

        when(electionCandidateRepository.getCandidatesByElectionId(electionId)).thenReturn(candidateViewList);

        Assert.assertEquals(candidatesOfAnElectionResult,electionCandidateService.getAllCandidates(electionId));
    }

    @Test(expected = ResponseStatusException.class)
    public void getByCandidateAndElectionNotFoundTest(){
        Integer electionId=1;
        Integer candidateId=2;

        when(electionCandidateRepository.getByCandidateAndElection(candidateId,electionId))
                .thenReturn(Optional.empty());

        electionCandidateService.getByCandidateAndElection(electionId,candidateId);
    }
    @Test
    public void getByCandidateAndElectionTest(){
        Integer electionId=1;
        Integer candidateId=2;
        Integer electionCandidateId=3;

        Election election = Election.builder()
                .id(electionId)
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .endDate(LocalDateTime.of(2020,2,3,14,0,0))
                .build();

        Candidate candidate = Candidate.builder()
                .id(candidateId)
                .name("Damian")
                .lastname("Tacconi")
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .id(electionCandidateId)
                .election(election)
                .candidate(candidate)
                .votes(new ArrayList<>())
                .build();

        when(electionCandidateRepository.getByCandidateAndElection(candidateId,electionId))
                .thenReturn(Optional.ofNullable(electionCandidate));

        Assert.assertEquals(electionCandidate,electionCandidateService.getByCandidateAndElection(electionId,candidateId));
    }
}
