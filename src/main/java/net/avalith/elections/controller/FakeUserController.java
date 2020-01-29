package net.avalith.elections.controller;

import net.avalith.elections.entities.CandidateVoteRequest;
import net.avalith.elections.entities.FakeUserQuantityRequest;
import net.avalith.elections.entities.MessageResponse;
import net.avalith.elections.service.UserService;
import net.avalith.elections.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/non-alcoholic-beer")
@RestController
public class FakeUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VoteService voteService;

    @PostMapping("")
    public MessageResponse addFakeUser(@RequestBody FakeUserQuantityRequest fakeUserRequest){
        return this.userService.generateFakeUser(fakeUserRequest.getQuantity());
    }

    @PostMapping("{id}")
    public MessageResponse vote(@PathVariable("id")Integer id, @RequestBody CandidateVoteRequest candidate){

        return this.voteService.fakeVote(id,candidate.getCandidateId());
    }
}
