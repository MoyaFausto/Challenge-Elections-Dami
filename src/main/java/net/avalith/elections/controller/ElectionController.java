package net.avalith.elections.controller;

import net.avalith.elections.entities.ElectionListResponse;
import net.avalith.elections.entities.ElectionRequest;
import net.avalith.elections.entities.ElectionResponse;
import net.avalith.elections.model.Election;
import net.avalith.elections.repository.ElectionRepository;
import net.avalith.elections.service.ElectionService;
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

@RequestMapping("/election")
@RestController
public class ElectionController {

    @Autowired
    private ElectionService electionService;


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

}
