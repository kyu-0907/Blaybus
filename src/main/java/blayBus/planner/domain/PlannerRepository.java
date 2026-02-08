package blayBus.planner.domain;

import blayBus.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long> {
    boolean existsByDateAndUserId(LocalDate date, long userId);

    Optional<Planner> findByDateAndUser(LocalDate date, User user);
}
