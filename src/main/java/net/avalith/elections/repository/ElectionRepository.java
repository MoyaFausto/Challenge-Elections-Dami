package net.avalith.elections.repository;

import net.avalith.elections.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Integer> {
    String queryGetActiveElections = "select * from elections where start_date < now() and end_date > now()";

    @Query(value = queryGetActiveElections, nativeQuery = true)
    List<Election> getActiveElections();
}
