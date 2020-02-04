package net.avalith.elections.service;

import net.avalith.elections.entities.CandidateListResponse;
import net.avalith.elections.entities.CandidateResponse;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.repository.CandidateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestCandidateService {

    @Autowired
    CandidateService candidateService;

    @Autowired
    @MockBean
    CandidateRepository candidateRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllTest(){
        List<Candidate> candidates = Arrays.asList(
                new Candidate(1,"Martin","Diaz", new ArrayList<>(), new ArrayList<>()),
                new Candidate(2,"Damian","Tacconi",new ArrayList<>(), new ArrayList<>()),
                new Candidate(3,"Daniel","Sciacco",new ArrayList<>(), new ArrayList<>()));


        //test

        when(candidateRepository.findAll()).thenReturn(candidates);
        CandidateListResponse candidateListResponse = candidateService.findAll();

        Assert.assertEquals(3,candidateListResponse.getCandidates().size());
        verify(candidateRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findByIdTest(){
        Integer id = 1;
        Candidate candidateExpected =  Candidate.builder().id(id).build();
        when(candidateRepository.findById(id)).thenReturn(Optional.of(candidateExpected));

        verify(candidateRepository,times(1)).findById(id);
        Assert.assertEquals(candidateExpected , candidateService.findById(id));
    }

    @Test
    public void saveTest(){

        Candidate candidate = Candidate.builder()
                .name("Damian")
                .lastname("Tacconi")
                .build();

        Candidate candidateSaved = Candidate.builder()
                .id(1)
                .name("Damian")
                .lastname("Tacconi")
                .build();

        CandidateResponse expected = new CandidateResponse(candidateSaved.getId());

        when(candidateRepository.save(candidate)).thenReturn(candidateSaved);

        Assert.assertEquals(expected,candidateService.save(candidate));
    }

    @Test
    public void deleteTest(){
        Candidate candidate = Candidate.builder()
                .id(1)
                .name("Damian")
                .lastname("Tacconi")
                .build();

        when(candidateRepository.findById(1)).thenReturn(Optional.ofNullable(candidate));

        candidateService.delete(1);
        verify(candidateRepository, times(1)).delete(candidate);
    }

    @Test
    public void updateTest(){
        Candidate newCandidate = Candidate.builder()
                .name("Martin")
                .lastname("Diaz")
                .build();

        Candidate oldCandidate = Candidate.builder()
                .id(1)
                .name("Damian")
                .lastname("Tacconi")
                .build();
        Integer id = 1;
        Candidate candidateUpdated = Candidate.builder().id(1).name("Martin").lastname("Diaz").build();


        when(candidateRepository.findById(id)).thenReturn(Optional.ofNullable(oldCandidate));
        assert oldCandidate != null;
        oldCandidate.setLastname(newCandidate.getLastname());
        oldCandidate.setName(newCandidate.getName());

        when(candidateRepository.save(oldCandidate)).thenReturn(candidateUpdated);

        candidateService.update(newCandidate,id);

        verify(candidateRepository,times(1)).findById(id);
        verify(candidateRepository,times(1)).save(oldCandidate);
    }

}
