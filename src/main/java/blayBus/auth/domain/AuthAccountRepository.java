package blayBus.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthAccountRepository extends JpaRepository<AuthAccount, Long> {
    @Query("select a from AuthAccount a where a.user.id = :userId")
    Optional<AuthAccount> findByUserId(@Param("userId") Long userId);

    Optional<AuthAccount> findByLoginId(String loginId);
}
