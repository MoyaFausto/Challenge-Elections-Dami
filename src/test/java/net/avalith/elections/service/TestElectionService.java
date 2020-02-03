package net.avalith.elections.service;

import net.avalith.elections.entities.ElectionListResponse;
import net.avalith.elections.entities.ElectionRequest;
import net.avalith.elections.entities.ElectionResponse;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionCandidate;
import net.avalith.elections.repository.ElectionRepository;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestElectionService {

    @Autowired
    ElectionService electionService;

    @Autowired
    @MockBean
    ElectionRepository electionRepository;

    @Autowired
    @MockBean
    CandidateService candidateService;

    @Autowired
    @MockBean
    ElectionCandidateService electionCandidateService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = ResponseStatusException.class)
    public void findByIdNullTest(){
        Integer id = 1;
        Election electionNull;

        when(electionRepository.findById(id)).thenReturn(Optional.empty());
        electionNull = electionService.findById(id);

        Assert.assertNull(electionNull);
    }

    @Test
    public void findByIdTest(){
        Integer id = 1;

        Election electionExpected = Election.builder()
                .id(id)
                .startDate(LocalDateTime.of(2020,7,31,20,00,00))
                .endDate(LocalDateTime.of(2020,12,31,20,00,00)).build();

        when(electionRepository.findById(id)).thenReturn(Optional.of(electionExpected));

        Election election = electionService.findById(id);

        Assert.assertEquals(electionExpected , election);
    }


    @Test
    public void deleteTest(){
        Integer id = 1;
        Election election = Election.builder()
                .id(id)
                .startDate(LocalDateTime.of(2020,12,31,20,00,00))
                .endDate(LocalDateTime.MAX)
                .build();

        when(electionRepository.findById(id)).thenReturn(Optional.ofNullable(election));

        electionService.delete(id);
        verify(electionRepository, times(1)).delete(election);
    }

    @Test(expected = ResponseStatusException.class)
    public void invalidDatesRangeTest(){
        ElectionRequest electionRequest = ElectionRequest.builder()
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .endDate(LocalDateTime.of(2020,1,3,14,0,0))
                .candidateIds(new HashSet<>(1,2))
                .build();

        electionService.save(electionRequest);

        Assert.assertTrue(electionRequest.getStartDate().isAfter(electionRequest.getEndDate()));
    }
    @Test
    public void saveTest(){

        Integer id = 1;
        Integer candidateId = 1;
        Integer electionCandidateId = 1;
        Set<Integer> candidateIds = new HashSet<>();
        candidateIds.add(candidateId);

        ElectionRequest electionRequest = ElectionRequest.builder()
                .endDate(LocalDateTime.of(2020,12,31,20,0,0))
                .startDate(LocalDateTime.of(2020,7,31,20,0,0))
                .candidateIds(candidateIds).build();

        Election election = Election.builder().electionCandidates(new ArrayList<>())
                .startDate(LocalDateTime.of(2020,7,31,20,0,0))
                .endDate(LocalDateTime.of(2020,12,31,20,0,0)).build();

        Election electionResponseDB = Election.builder()
                .id(id)
                .electionCandidates(new ArrayList<>())
                .startDate(LocalDateTime.of(2020,7,31,20,0, 0))
                .endDate(LocalDateTime.of(2020,12,31,20,0,0)).build();

        Candidate candidate = Candidate.builder().electionCandidates(new ArrayList<>())
                .id(candidateId)
                .name("Damian")
                .lastname("Tacconi").build();

        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .election(electionResponseDB)
                .candidate(candidate)
                .build();

        when(candidateService.findById(candidateId)).thenReturn(candidate);
        when(electionCandidateService.save(electionCandidate)).thenReturn(electionCandidateId);
        when(electionRepository.save(election)).thenReturn(electionResponseDB);

        ElectionResponse expected = new ElectionResponse(electionResponseDB.getId());

        Assert.assertEquals(expected,electionService.save(electionRequest));
    }

    @Test
    public void updateTest(){
        Integer id = 1;
        Election newElection = Election.builder()
                .startDate(LocalDateTime.of(2020,12,31,20,0,0))
                .endDate(LocalDateTime.MAX)
                .electionCandidates(new ArrayList<>())
                .build();

        Election oldElection = Election.builder()
                .id(id)
                .startDate(LocalDateTime.of(2020,12,31,20,0,0))
                .electionCandidates(new ArrayList<>())
                .endDate(LocalDateTime.MAX)
                .build();

        Election electionUpdated = Election.builder().id(id).endDate(LocalDateTime.MAX).startDate(LocalDateTime.of(2020,12,31,20,0,0)).build();

        Mockito.when(electionRepository.findById(id)).thenReturn(Optional.ofNullable(oldElection));
        assert oldElection != null;
        oldElection.setElectionCandidates(newElection.getElectionCandidates());
        oldElection.setEndDate(newElection.getEndDate());
        oldElection.setStartDate(newElection.getStartDate());

        Mockito.when(electionRepository.save(oldElection)).thenReturn(electionUpdated);

        electionService.update(newElection,id);

        Mockito.verify(electionRepository,times(1)).findById(id);
        Mockito.verify(electionRepository,times(1)).save(oldElection);
    }

    @Test
    public void findAllTest(){
        List<Election> elections = Arrays.asList(
                Election.builder().id(1).startDate(LocalDateTime.of(2020,12,31,20,00,00)).endDate(LocalDateTime.MAX).build()
        );
        //test
        Mockito.when(electionRepository.findAll()).thenReturn(elections);
        ElectionListResponse electionListResponse = electionService.findAll();

        Assert.assertEquals(1,electionListResponse.getElections().size());
        Mockito.verify(electionRepository, Mockito.times(1)).findAll();
    }
    @Test
    public void getActiveElectionsTest(){
        List<Election> elections = Arrays.asList(
                Election.builder().id(1).startDate(LocalDateTime.of(2020,1,31,20,0,0)).endDate(LocalDateTime.MAX).build()
        );
        //test
        Mockito.when(electionRepository.getActiveElections()).thenReturn(elections);
        List<Election> electionList = electionService.getActiveElections();

        Assert.assertEquals(1,electionList.size());
        Mockito.verify(electionRepository, Mockito.times(1)).getActiveElections();
    }
}
