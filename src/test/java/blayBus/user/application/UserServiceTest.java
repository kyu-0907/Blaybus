package blayBus.user.application;

import blayBus.user.application.exception.UserNotFoundException;
import blayBus.user.domain.User;
import blayBus.user.domain.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    UserService userService;

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserRepository userRepository;

    @DisplayName("존재하지 않는 사용자 조회 시 예외가 발생한다.")
    @Test
    void findUserException() {
        assertThatThrownBy(() -> userService.findUserById(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("userId로 사용자를 조회할 수 있다.")
    @Test
    void findUserById() {
        User user = userRepository.save(createTestUser());
        em.flush();
        em.clear();

        User foundUser = userService.findUserById(user.getId());

        assertThat(foundUser.getId()).isEqualTo(user.getId());
    }

    @DisplayName("사용자 이름 변경 시 이름은 필수 입력 사항이다.")
    @Test
    void changeNameException() {
        User user = userRepository.save(createTestUser());
        em.flush();
        em.clear();

        assertThatThrownBy(() -> userService.changeUserName(user.getId(), ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("사용자 이름을 변경할 수 있다.")
    @Test
    void changeUserName() {
        User user = userRepository.save(createTestUser());
        em.flush();
        em.clear();

        userService.changeUserName(user.getId(), "newName");
        em.flush();
        em.clear();

        User found = userRepository.findById(user.getId())
                .orElseThrow();
        assertThat(found.getName()).isEqualTo("newName");
    }

    @DisplayName("없는 사용자를 삭제할 수 없다.")
    @Test
    void deleteNotExistUser() {
        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("userId를 사용해 사용자를 삭제할 수 있다.")
    @Test
    void deleteUserById() {
        User user = userRepository.save(createTestUser());
        em.flush();
        em.clear();

        userService.deleteUser(user.getId());
        em.flush();
        em.clear();

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    User createTestUser() {
        return User.createMentee("test");
    }
}