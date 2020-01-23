package net.avalith.elections.controller;

import net.avalith.elections.model.Candidate;
import net.avalith.elections.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RequestMapping("api/candidates")
@RestController
public class CandidateController {

    private final String CANDIDATE_NOT_FOUND = "The candidate was not found";

    @Autowired
    private CandidateService candidateService;


    @PostMapping("")
    public Candidate save(@Validated @RequestBody Candidate candidate){
        return this.candidateService.save(candidate);
    }

    @GetMapping("{id}")
    public Candidate findById(@PathVariable("id") int id){
        return this.candidateService.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, this.CANDIDATE_NOT_FOUND));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") int id){
        if(!this.candidateService.delete(id)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, this.CANDIDATE_NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public void update(@Validated @RequestBody Candidate candidate , @PathVariable("id") int id){
        if(!this.candidateService.update(candidate , id)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST , this.CANDIDATE_NOT_FOUND);
        }
    }
    @GetMapping("")
    public List<Candidate> findAll(){
        return this.candidateService.findAll();
    }

}