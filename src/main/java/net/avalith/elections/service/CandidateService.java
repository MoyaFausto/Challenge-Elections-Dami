package net.avalith.elections.service;

import net.avalith.elections.model.Candidate;
import net.avalith.elections.repository.CandidateJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateJpaRepository candidateJpaRepository;

    public Candidate save(Candidate candidate){
        return this.candidateJpaRepository.save(candidate);
    }

    public Optional<Candidate> findById(int id){
        return this.candidateJpaRepository.findById(id);
    }

    public boolean delete(int id){
        Optional<Candidate> optionalCandidate = this.candidateJpaRepository.findById(id);
        if(optionalCandidate.isPresent()){
            this.candidateJpaRepository.delete(optionalCandidate.get());
            return true;
        }
        return false;
    }

    public boolean update(Candidate candidate , int id){
        Optional<Candidate> optionalCandidate = this.candidateJpaRepository.findById(id);
        if(optionalCandidate.isPresent()){
            Candidate oldCandidate = optionalCandidate.get();
            oldCandidate.setName(candidate.getName());
            oldCandidate.setLastname(candidate.getLastname());
            this.candidateJpaRepository.save(oldCandidate);
            return true;
        }
        return false;
    }
    public List<Candidate> findAll(){
        return this.candidateJpaRepository.findAll();
    }


}