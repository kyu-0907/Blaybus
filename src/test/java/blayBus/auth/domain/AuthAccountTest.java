package blayBus.auth.domain;

import blayBus.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class AuthAccountTest {
    @Test
    void getUserId() {
        User user = User.createMentee("testName");
        ReflectionTestUtils.setField(user, "id", 1L);
        AuthAccount authAccount = AuthAccount.of("loginId", "encryptedPassword", user);

        assertThat(authAccount.getUserId()).isEqualTo(String.valueOf(user.getId()));
    }

    @Test
    void getRole() {
        User user = User.createMentee("testName");
        AuthAccount authAccount = AuthAccount.of("loginId", "encryptedPassword", user);

        assertThat(authAccount.getRole()).isEqualTo("ROLE_MENTEE");
    }
}