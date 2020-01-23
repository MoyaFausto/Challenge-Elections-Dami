package net.avalith.elections.controller;

import net.avalith.elections.model.User;
import net.avalith.elections.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RequestMapping("api/users")
@RestController
public class UserController {

    private final String USER_NOT_FOUND = "The user was not found";

    @Autowired
    private UserService userService;


    @PostMapping("")
    public User save(@Validated @RequestBody User user){
        return this.userService.save(user);
    }

    @GetMapping("{id}")
    public User findById(@PathVariable("id") int id){
       return this.userService.findById(id)
               .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, this.USER_NOT_FOUND));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") int id){
        if(!this.userService.delete(id)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, this.USER_NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public void update(@Validated @RequestBody User user , @PathVariable("id") int id){
        if(!this.userService.update(user , id)){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST , this.USER_NOT_FOUND);
        }
    }
    @GetMapping("")
    public List<User> findAll(){
        return this.userService.findAll();
    }

}
