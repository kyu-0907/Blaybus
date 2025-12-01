package politicConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import politicConnect.domain.Provider;

import java.util.Optional;

public interface OAuthIdentityRepository extends JpaRepository<OAuthIdentity, Long> {

    Optional<OAuthIdentity> findByProviderAndProviderUserId(Provider provider, String providerUserId);
    boolean existsByUserIdAndProvider(Long userId, Provider provider);
}
