package net.avalith.elections.service;

import net.avalith.elections.entities.CandidateVotes;
import net.avalith.elections.entities.ElectionVotes;
import net.avalith.elections.entities.MessageResponse;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.model.Election;
import net.avalith.elections.model.ElectionCandidate;
import net.avalith.elections.model.User;
import net.avalith.elections.model.Vote;
import net.avalith.elections.repository.ElectionCandidateRepository;
import net.avalith.elections.repository.ElectionRepository;
import net.avalith.elections.repository.UserRepository;
import net.avalith.elections.repository.VoteRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class TestVoteService {

    @Spy
    @Autowired
    VoteService voteService;

    @Autowired
    @MockBean
    private
    ElectionCandidateService electionCandidateService;

    @Autowired
    @MockBean
    UserRepository userRepository;

    @Autowired
    @MockBean
    private
    UserService userService;

    @Autowired
    @MockBean
    private
    ElectionService electionService;

    @Autowired
    @MockBean
    ElectionCandidateRepository electionCandidateRepository;

    @Autowired
    @MockBean
    ElectionRepository electionRepository;

    @Autowired
    @MockBean
    private
    VoteRepository voteRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByIdTest(){
        Integer id = 1;
        Vote voteExpected =  Vote.builder().id(id).build();
        when(voteRepository.findById(id)).thenReturn(Optional.of(voteExpected));

        verify(voteRepository,times(1)).findById(id);
        Assert.assertEquals(voteExpected , voteService.findById(id));
    }

    @Test
    public void fakeVoteTest(){
        Integer electionId = 1;
        Integer candidateId = 2;
        Integer electionCandidateId = 5;

        List<User> fakeUsersResponse = Arrays.asList(User.builder().votes(new ArrayList<>()).isFake(1)
                .id("0c358357-620d-4947-983c-16d99ee34fca")
                .dni(232443)
                .name("Cosme")
                .lastname("Fulanito")
                .votes(new ArrayList<>())
                .build());
        Election election = Election.builder()
                .id(electionId)
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .endDate(LocalDateTime.of(2020,2,4,14,0,0))
                .electionCandidates(new ArrayList<>())
                .build();

        Candidate candidate = Candidate.builder()
                .id(2)
                .lastname("Tacconi")
                .name("Damian")
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .election(election)
                .candidate(candidate)
                .id(electionCandidateId)
                .votes(new ArrayList<>())
                .build();

        MessageResponse messageResponseVote = new MessageResponse("Voto ingresado con exito");

        election.getElectionCandidates().add(electionCandidate);
        candidate.getElectionCandidates().add(electionCandidate);
        Vote vote = Vote.builder().user(fakeUsersResponse.get(0)).electionCandidate(electionCandidate).build();

        when(userService.findFakes()).thenReturn(fakeUsersResponse);
        when(userService.findById(fakeUsersResponse.get(0).getId())).thenReturn(fakeUsersResponse.get(0));
        when(electionService.findById(electionId)).thenReturn(election);
        when(electionCandidateService.getByCandidateAndElection(electionId,candidateId)).thenReturn(electionCandidate);

        MessageResponse messageResponse = voteService.fakeVote(electionId,candidateId);

        verify(voteRepository,times(1)).save(vote);
        Assert.assertEquals("Voto ingresado con exito", messageResponseVote.getMessage());
        Assert.assertEquals("Votos generados correctamente", messageResponse.getMessage());
    }
    @Test
    public void getElectionResult(){
        Integer electionId = 4;
        Election electionResponse = Election.builder()
                .id(electionId)
                .startDate(LocalDateTime.of(2020,2,3,14,0,0))
                .endDate(LocalDateTime.of(2020,2,4,14,0,0))
                .electionCandidates(new ArrayList<>())
                .build();

        Candidate candidate = Candidate.builder()
                .id(2)
                .lastname("Tacconi")
                .name("Damian")
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .election(electionResponse)
                .candidate(candidate)
                .id(5)
                .votes(new ArrayList<>())
                .build();

        electionResponse.getElectionCandidates().add(electionCandidate);
        candidate.getElectionCandidates().add(electionCandidate);

        List<CandidateVotes> candidateVotes = new ArrayList<>();

        candidateVotes.add(CandidateVotes.builder()
                .idCandidate(candidate.getId())
                .firstName("Damian")
                .lastName("Tacconi")
                .votes(0)
                .build());

        Integer totalVotes = 0;

        ElectionVotes electionVotes = ElectionVotes.builder()
                .totalVotes(totalVotes)
                .idElection(electionId)
                .candidates(candidateVotes)
                .build();

        when(electionService.findById(electionId)).thenReturn(electionResponse);
        Assert.assertEquals(electionVotes,voteService.getElectionResult(electionId));
    }

    @Test(expected = ResponseStatusException.class)
    public void fakeUsersCanNotVoteTest(){
        Integer electionId = 1;
        Integer candidateId = 2;
        Integer electionCandidateId = 5;

        List<User> fakeUsersResponse = Collections.singletonList(User.builder().votes(new ArrayList<>()).isFake(1)
                .id("0c358357-620d-4947-983c-16d99ee34fca")
                .dni(232443)
                .name("Cosme")
                .lastname("Fulanito")
                .votes(new ArrayList<>())
                .build());

        Election election = Election.builder()
                .id(electionId)
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .endDate(LocalDateTime.of(2020,2,4,14,0,0))
                .electionCandidates(new ArrayList<>())
                .build();

        Candidate candidate = Candidate.builder()
                .id(candidateId)
                .lastname("Tacconi")
                .name("Damian")
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .election(election)
                .candidate(candidate)
                .id(electionCandidateId)
                .votes(new ArrayList<>())
                .build();

        candidate.getElectionCandidates().add(electionCandidate);
        election.getElectionCandidates().add(electionCandidate);

        Vote vote = Vote.builder().user(fakeUsersResponse.get(0)).electionCandidate(electionCandidate).build();
        fakeUsersResponse.get(0).getVotes().add(vote);

        when(userService.findFakes()).thenReturn(fakeUsersResponse);

        voteService.fakeVote(electionId,candidateId);
    }

    @Test(expected = ResponseStatusException.class)
    public void electionIsNotActiveTest(){
        String userId = "0c358357-620d-4947-983c-16d99ee34fca";
        Integer electionId = 1;
        Integer candidateId = 2;

        User user = User.builder()
                .id(userId)
                .dni(233423)
                .name("Damian")
                .votes(new ArrayList<>())
                .lastname("Tacconi")
                .build();

        Election election = Election.builder().id(electionId)
                .startDate(LocalDateTime.of(2020,1,1,14,0,0))
                .endDate(LocalDateTime.of(2020,1,2,14,0,0))
                .build();

        when(userService.findById(userId)).thenReturn(user);
        when(electionService.findById(electionId)).thenReturn(election);

        voteService.save(electionId,candidateId,userId);
    }

    @Test(expected = ResponseStatusException.class)
    public void userHasAlreadyVotedTest(){
        String userId = "0c358357-620d-4947-983c-16d99ee34fca";
        Integer electionId = 1;
        Integer candidateId = 2;

        User user = User.builder()
                .id(userId)
                .dni(233423)
                .name("Damian")
                .votes(new ArrayList<>())
                .lastname("Tacconi")
                .build();

        Election election = Election.builder()
                .id(electionId)
                .startDate(LocalDateTime.of(2020,2,3,14,0,0))
                .endDate(LocalDateTime.of(2020,2,4,14,0,0))
                .electionCandidates(new ArrayList<>())
                .build();

        Candidate candidate = Candidate.builder()
                .id(candidateId)
                .lastname("Tacconi")
                .name("Damian")
                .electionCandidates(new ArrayList<>())
                .build();

        ElectionCandidate electionCandidate = ElectionCandidate.builder()
                .election(election)
                .candidate(candidate)
                .id(5)
                .votes(new ArrayList<>())
                .build();

        election.getElectionCandidates().add(electionCandidate);
        candidate.getElectionCandidates().add(electionCandidate);

        Vote vote = Vote.builder().user(user).electionCandidate(electionCandidate).build();
        user.getVotes().add(vote);

        when(userService.findById(userId)).thenReturn(user);

        voteService.save(electionId,candidateId,userId);
    }

    @Test
    public void saveTest(){
        String userId = "0c358357-620d-4947-983c-16d99ee34fca";
        Integer electionId = 1;
        Integer candidateId = 2;
        Integer electionCandidateId = 3;
        User userResponse = User.builder().dni(3213123)
                .lastname("Tacconi")
                .name("Damian")
                .isFake(0)
                .id(userId)
                .votes(new ArrayList<>())
                .build();

        Candidate candidate = Candidate.builder()
                .id(candidateId)
                .name("Juan")
                .lastname("Perez")
                .electionCandidates(new ArrayList<>())
                .build();

        Election election = Election.builder()
                .id(electionId)
                .electionCandidates(new ArrayList<>())
                .endDate(LocalDateTime.of(2020,2,2,15,0,0))
                .startDate(LocalDateTime.of(2020,2,2,14,0,0))
                .build();

        ElectionCandidate electionCandidateResponse = ElectionCandidate.builder()
                .id(electionCandidateId)
                .candidate(candidate)
                .election(election)
                .votes(new ArrayList<>())
                .build();

        candidate.getElectionCandidates().add(electionCandidateResponse);
        election.getElectionCandidates().add(electionCandidateResponse);

        Vote vote = Vote.builder().user(userResponse).electionCandidate(electionCandidateResponse).build();

        when(voteService.isActiveElection(election)).thenReturn(true);
        when(electionService.findById(electionId)).thenReturn(election);
        when(userService.findById(userId)).thenReturn(userResponse);
        when(electionCandidateService.getByCandidateAndElection(electionId,candidateId))
                .thenReturn(electionCandidateResponse);


        MessageResponse messageResponse = voteService.save(electionId,candidateId,userId);

        Assert.assertEquals("Voto ingresado con exito" , messageResponse.getMessage());
        verify(voteRepository,times(1)).save(vote);
    }

    @Test
    public void updateTest(){
        Integer id = 1;

        Vote newVote = Vote.builder()
                .build();
        Vote oldVote = Vote.builder()
                .id(id)
                .build();
        Vote userUpdated = Vote.builder().id(id).build();


        when(voteRepository.findById(id)).thenReturn(Optional.ofNullable(oldVote));
        assert oldVote != null;
        oldVote.setElectionCandidate(newVote.getElectionCandidate());
        oldVote.setUser(newVote.getUser());

        when(voteRepository.save(oldVote)).thenReturn(userUpdated);

        voteService.update(newVote,id);

        verify(voteRepository,times(1)).findById(id);
        verify(voteRepository,times(1)).save(oldVote);
    }

    @Test
    public void deleteTest(){
        Integer voteId = 1;
        Vote vote = Vote.builder().id(voteId).build();

        when(voteRepository.findById(voteId)).thenReturn(Optional.ofNullable(vote));

        voteService.delete(voteId);
        verify(voteRepository, times(1)).delete(vote);
    }

    @Test(expected = ResponseStatusException.class)
    public void findByIdNullTest(){
        Integer id = 1;
        Vote voteNull;

        when(voteRepository.findById(id)).thenReturn(Optional.empty());
        voteNull = voteService.findById(id);

        Assert.assertNull(voteNull);
    }
}
