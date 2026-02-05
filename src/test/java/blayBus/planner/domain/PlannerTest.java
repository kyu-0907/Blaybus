package blayBus.planner.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static blayBus.planner.domain.Planner.create;
import static blayBus.user.domain.User.createMentee;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlannerTest {
    @Test
    void plannerRequiredDate() {
        assertThatThrownBy(() -> create(null, createMentee("testName")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void plannerRequiredUser() {
        assertThatThrownBy(() -> create(LocalDate.now(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("하루 총 공부 시간은 0분 이상이어야 한다.")
    @Test
    void setTotalStudyTimeMin() {
        Planner planner = create(LocalDate.now(), createMentee("testName"));

        assertThatThrownBy(() -> planner.setTotalStudyTime(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("하루 총 공부 시간은 24시간 이하여야 한다.")
    @Test
    void setTotalStudyTimeMax() {
        Planner planner = create(LocalDate.now(), createMentee("testName"));

        assertThatThrownBy(() -> planner.setTotalStudyTime(1441))
                .isInstanceOf(IllegalArgumentException.class);
    }
}