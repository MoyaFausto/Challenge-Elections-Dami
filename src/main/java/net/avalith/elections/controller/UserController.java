package net.avalith.elections.controller;

import net.avalith.elections.entities.UserListResponse;
import net.avalith.elections.entities.UserResponse;
import net.avalith.elections.model.User;
import net.avalith.elections.service.UserService;
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

@RequestMapping("/user")
@RestController
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("")
    public UserResponse save(@Validated @RequestBody User user){

        return this.userService.save(user);
    }

    @GetMapping("{id}")
    public User findById(@PathVariable("id") Integer id){

       return this.userService.findById(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id){

        this.userService.delete(id);
    }

    @PutMapping("{id}")
    public void update(@Validated @RequestBody User user , @PathVariable("id") Integer id){

        this.userService.update(user,id);
    }

    @GetMapping("")
    public UserListResponse findAll(){

        return this.userService.findAll();
    }

}
