package blayBus.planner.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long> {
    boolean existsByDateAndUserId(LocalDate date, long userId);
}
