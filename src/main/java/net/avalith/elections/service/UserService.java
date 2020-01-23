package net.avalith.elections.service;

import net.avalith.elections.model.User;
import net.avalith.elections.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    public User save(User user){
        return this.userJpaRepository.save(user);
    }

    public Optional<User> findById(int id){
        return this.userJpaRepository.findById(id);
    }

    public boolean delete(int id){
        Optional<User> optionalUser = this.userJpaRepository.findById(id);
        if(optionalUser.isPresent()){
            this.userJpaRepository.delete(optionalUser.get());
            return true;
        }
        return false;
    }

    public boolean update(User user , int id){
        Optional<User> optionalUser = this.userJpaRepository.findById(id);
        if(optionalUser.isPresent()){
            User oldUser = optionalUser.get();
            oldUser.setDni(user.getDni());
            oldUser.setName(user.getName());
            this.userJpaRepository.save(oldUser);
            return true;
        }
        return false;
    }
    public List<User> findAll(){
        return this.userJpaRepository.findAll();
    }


}
