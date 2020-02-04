package net.avalith.elections.repository;

import net.avalith.elections.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User , String> {

    List<User> findByIsFake(Integer isfake);
}
