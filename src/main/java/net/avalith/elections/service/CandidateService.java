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

        UUID id = UUID.randomUUID();
        String idString = id.toString();
        candidate.setId(idString);
        this.candidateRepository.save(candidate);
        return new CandidateResponse(idString);
    }

    public Candidate findById(String id){

        return this.candidateRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, ErrorMessage.CANDIDATE_NOT_FOUND));
    }

    public void delete(String id){

        Candidate candidate= this.candidateRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, ErrorMessage.CANDIDATE_NOT_FOUND));
        this.candidateRepository.delete(candidate);
    }

    public void update(Candidate candidate , String id){


        Candidate oldCandidate= this.candidateRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, ErrorMessage.CANDIDATE_NOT_FOUND));
        oldCandidate.setName(candidate.getName());
        oldCandidate.setLastname(candidate.getLastname());
        this.candidateRepository.save(oldCandidate);

    }
    public CandidateListResponse findAll(){
        return new CandidateListResponse(this.candidateRepository.findAll());
    }


}