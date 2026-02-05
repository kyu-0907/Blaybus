package blayBus.planner.domain;

import blayBus.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int totalStudyTime;

    public static Planner create(LocalDate date, User user) {
        Assert.notNull(date, "Date must not be null");
        Assert.notNull(user, "User must not be null");

        Planner planner = new Planner();
        planner.date = date;
        planner.user = user;
        planner.totalStudyTime = 0;

        return planner;
    }

    public void setTotalStudyTime(int minutes) {
        Assert.isTrue(minutes >= 0, "Minutes must be greater than or equal to zero");
        Assert.isTrue(minutes <= 1440, "Minutes must be less than or equal to 1440");

        this.totalStudyTime = minutes;
    }
}