package politicConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import politicConnect.domain.Provider;
import politicConnect.domain.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByLocalLoginId(String localLoginId);

    boolean existsByNickName(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByLocalLoginId(String localLoginId);

    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByProviderAndUserId(Provider provider, String userId);


}
