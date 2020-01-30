package net.avalith.elections.controller;

import net.avalith.elections.entities.CandidateVoteRequest;
import net.avalith.elections.entities.CandidatesOfAnElectionResult;
import net.avalith.elections.entities.ElectionListResponse;
import net.avalith.elections.entities.ElectionRequest;
import net.avalith.elections.entities.ElectionResponse;
import net.avalith.elections.entities.ElectionVotes;
import net.avalith.elections.entities.MessageResponse;
import net.avalith.elections.model.Election;
import net.avalith.elections.service.ElectionCandidateService;
import net.avalith.elections.service.ElectionHistoryService;
import net.avalith.elections.service.ElectionService;
import net.avalith.elections.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/election")
@RestController
public class ElectionController {

    @Autowired
    private ElectionService electionService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private ElectionCandidateService electionCandidateService;

    @Autowired
    private ElectionHistoryService electionHistoryService;


    @PostMapping("")
    public ElectionResponse save(@Validated @RequestBody ElectionRequest election){

        return this.electionService.save(election);
    }

    @GetMapping("{id}")
    public Election findById(@PathVariable("id") Integer id){
        return this.electionService.findById(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id){

        this.electionService.delete(id);
    }

    @PutMapping("{id}")
    public void update(@Validated @RequestBody Election election , @PathVariable("id") Integer id){

        this.electionService.update(election, id);
    }

    @GetMapping("")
    public ElectionListResponse findAll(){

        return this.electionService.findAll();
    }

    @GetMapping("{id}/candidate")
    public CandidatesOfAnElectionResult getAllCandidates(@PathVariable("id") Integer id){

        return this.electionCandidateService.getAllCandidates(id);
    }

    @PostMapping("{id}/vote")
    public MessageResponse vote(
            @PathVariable("id") Integer idElection,
            @RequestHeader("USER_ID") String userId,
            @RequestBody CandidateVoteRequest candidateId){

        return this.voteService.save(idElection,candidateId.getCandidateId(),userId);
    }

    @GetMapping("election")
    public ElectionVotes getElectionResult(@RequestParam Integer id_election){

       return this.voteService.getElectionResult(id_election);
    }

    @Scheduled(fixedRateString = "${history.time}")
    public void generateHistory(){

        this.electionHistoryService.generateHistory();
    }
}
