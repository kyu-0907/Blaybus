package blayBus.auth.service;

import blayBus.auth.domain.AuthAccount;
import blayBus.auth.domain.AuthAccountRepository;
import blayBus.user.domain.User;
import blayBus.user.domain.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AuthServiceTest {
    @Autowired
    AuthService authService;

    @Autowired
    AuthAccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    @Test
    void loginFail() {
        User user = userRepository.save(User.createMentee("testName"));
        accountRepository.save(AuthAccount.of("loginId", "password", user));
        em.flush();
        em.clear();

        assertThatThrownBy(() -> authService.login("wrongId", "password"))
                .isInstanceOf(BadCredentialsException.class);
        assertThatThrownBy(() -> authService.login("loginId", "wrongPassword"))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void loginSuccess() {
        User user = userRepository.save(User.createMentee("testName"));
        String encodedPassword = passwordEncoder.encode("password");
        accountRepository.save(AuthAccount.of("loginId", encodedPassword, user));
        em.flush();
        em.clear();

        String token = authService.login("loginId", "password");

        assertThat(token).isNotBlank();
        System.out.println("Generated Token: " + token);
    }
}