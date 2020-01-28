package net.avalith.elections.repository;

import net.avalith.elections.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Integer> {
}
