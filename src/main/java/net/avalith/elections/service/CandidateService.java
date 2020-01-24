package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.entities.CandidateListResponse;
import net.avalith.elections.entities.CandidateResponse;
import net.avalith.elections.model.Candidate;
import net.avalith.elections.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public CandidateResponse save(Candidate candidate){

        return new CandidateResponse(this.candidateRepository.save(candidate).getId());
    }

    public Candidate findById(Integer id){

        return this.candidateRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, ErrorMessage.CANDIDATE_NOT_FOUND));
    }

    public void delete(Integer id){

        Candidate candidate = this.findById(id);
        this.candidateRepository.delete(candidate);
    }

    public void update(Candidate newCandidate,  Integer id){

        Candidate oldCandidate = this.findById(id);
        oldCandidate.setName(newCandidate.getName());
        oldCandidate.setLastname(newCandidate.getLastname());
        this.candidateRepository.save(oldCandidate);

    }
    public CandidateListResponse findAll(){
        return new CandidateListResponse(this.candidateRepository.findAll());
    }


}