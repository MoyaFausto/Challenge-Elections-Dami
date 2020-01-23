package net.avalith.elections.service;

import net.avalith.elections.Utils.ErrorMessage;
import net.avalith.elections.entities.UserListResponse;
import net.avalith.elections.model.User;
import net.avalith.elections.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userJpaRepository;

    public User save(User user){

        UUID id = UUID.randomUUID();
        user.setId(id);
        return this.userJpaRepository.save(user);
    }

    public User findById(Integer id){

        return this.userJpaRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND));
    }

    public void delete(Integer id){

        User user= this.userJpaRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND));
        this.userJpaRepository.delete(user);
    }

    public void update(User user , Integer id){


        User oldUser= this.userJpaRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND));
        oldUser.setDni(user.getDni());
        oldUser.setName(user.getName());
        this.userJpaRepository.save(oldUser);

    }
    public UserListResponse findAll(){
        return new UserListResponse(this.userJpaRepository.findAll());
    }


}
