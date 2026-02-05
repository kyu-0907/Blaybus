package blayBus.auth.domain;

import blayBus.user.domain.User;
import blayBus.user.domain.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AuthAccountRepositoryTest {
    @Autowired
    AuthAccountRepository authAccountRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void findByUserId() {
        User user = userRepository.save(User.createMentee("testName"));
        authAccountRepository.save(AuthAccount.of("loginId", "encryptedPassword", user));
        em.flush();
        em.clear();

        AuthAccount authAccount = authAccountRepository.findByUserId(user.getId())
                .orElseThrow();

        assertThat(authAccount.getLoginId()).isEqualTo("loginId");
        assertThat(authAccount.getEncryptedPassword()).isEqualTo("encryptedPassword");
        assertThat(authAccount.getUserId()).isEqualTo(String.valueOf(user.getId()));
    }

    @Test
    void findByLoginId() {
        User user = User.createMentee("testName");
        userRepository.save(user);
        AuthAccount authAccount = AuthAccount.of("loginId", "encryptedPassword", user);
        authAccountRepository.save(authAccount);
        em.flush();
        em.clear();

        AuthAccount found = authAccountRepository.findByLoginId("loginId")
                .orElseThrow();

        assertThat(found.getLoginId()).isEqualTo("loginId");
        assertThat(found.getEncryptedPassword()).isEqualTo("encryptedPassword");
    }
}