package net.avalith.elections.repository;

import net.avalith.elections.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User , Integer> {
}
