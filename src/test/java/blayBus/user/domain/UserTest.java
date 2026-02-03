package blayBus.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static blayBus.user.domain.User.createMentee;
import static blayBus.user.domain.User.createMentor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {
    @DisplayName("멘토 생성 시 이름은 필수 조건이다.")
    @Test
    void createMentorNameException() {
        assertThatThrownBy(() -> createMentor(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("멘티 생성 시 이름은 필수 조건이다.")
    @Test
    void createMenteeNameException() {
        assertThatThrownBy(() -> createMentee(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름 변경 시 이름은 필수 조건이다.")
    @Test
    void changeNameException() {
        User user = createMentor("oldName");

        assertThatThrownBy(() -> user.changeName(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("사용자는 이름을 변경할 수 있다.")
    @Test
    void userCanChangeName() {
        User user = createMentor("oldName");

        user.changeName("newName");

        assertThat(user.getName()).isEqualTo("newName");
    }
}