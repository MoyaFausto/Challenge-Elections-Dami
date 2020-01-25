package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.model.UserByElectionCandidate;
import net.avalith.elections.repository.UserByElectionCandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserByElectionCandidateService {
    @Autowired
    private UserByElectionCandidateRepository userByElectionCandidateRepository;

    public Integer save(UserByElectionCandidate electionCandidate){


        electionCandidate = this.userByElectionCandidateRepository.save(electionCandidate);

        return electionCandidate.getId();
    }

    public UserByElectionCandidate findById(Integer id){

        return this.userByElectionCandidateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND));
    }

    public void delete(Integer id)
    {

        UserByElectionCandidate electionCandidate = this.findById(id);
        this.userByElectionCandidateRepository.delete(electionCandidate);
    }

    public void update(UserByElectionCandidate newUserByElectionCandidate, Integer id){

        UserByElectionCandidate oldUserByElectionCandidate = this.findById(id);
        oldUserByElectionCandidate.setElectionCandidate(newUserByElectionCandidate.getElectionCandidate());
        oldUserByElectionCandidate.setUser(newUserByElectionCandidate.getUser());

        this.userByElectionCandidateRepository.save(oldUserByElectionCandidate);
    }

}
