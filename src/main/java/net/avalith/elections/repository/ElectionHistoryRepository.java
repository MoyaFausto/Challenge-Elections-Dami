package net.avalith.elections.repository;

import net.avalith.elections.model.ElectionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionHistoryRepository extends JpaRepository<ElectionHistory, Integer> {
}
