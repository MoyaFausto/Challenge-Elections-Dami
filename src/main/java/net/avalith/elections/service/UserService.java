package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.entities.UserListResponse;
import net.avalith.elections.entities.UserResponse;
import net.avalith.elections.model.User;
import net.avalith.elections.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userJpaRepository;

    public UserResponse save(User user){

        user.setId(UUID.randomUUID().toString());
        user.setIsFake(0);
        this.userJpaRepository.save(user);

        return new UserResponse(user.getId());
    }

    public void save(List<User> users){
        this.userJpaRepository.saveAll(users);
    }

    public User findById(String id){

        return this.userJpaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND));
    }

    public void delete(String id)
    {

        User user = this.findById(id);
        this.userJpaRepository.delete(user);
    }

    public void update(User newUser, String id){

        User oldUser = this.findById(id);
        oldUser.setDni(newUser.getDni());
        oldUser.setName(newUser.getName());
        oldUser.setLastname(newUser.getLastname());
        this.userJpaRepository.save(oldUser);
    }
    public UserListResponse findAll(){

        return new UserListResponse(this.userJpaRepository.findAll());
    }

    public List<User> findFakes(){
        return this.userJpaRepository.findByIsFake(1);
    }

}
