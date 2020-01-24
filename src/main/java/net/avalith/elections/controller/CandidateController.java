package net.avalith.elections.controller;

import net.avalith.elections.entities.CandidateListResponse;
import net.avalith.elections.entities.CandidateResponse;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/candidate")
@RestController
public class CandidateController {


    @Autowired
    private CandidateService candidateService;


    @PostMapping("")
    public CandidateResponse save(@Validated @RequestBody Candidate candidate){
        return this.candidateService.save(candidate);
    }

    @GetMapping("{id}")
    public Candidate findById(@PathVariable("id") Integer id){

        return this.candidateService.findById(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id) {

        this.candidateService.delete(id);
    }

    @PutMapping("{id}")
    public void update(@Validated @RequestBody Candidate newCandidate , @PathVariable("id") Integer id){

        this.candidateService.update(newCandidate, id);
    }

    @GetMapping("")
    public CandidateListResponse findAll(){

        return this.candidateService.findAll();
    }

}