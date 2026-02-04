package blayBus.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public static User createMentor(String name) {
        return new User(name, UserRole.MENTOR);
    }

    public static User createMentee(String name) {
        return new User(name, UserRole.MENTEE);
    }

    private User(String name, UserRole role) {
        Assert.hasText(name, "Name must not be empty");

        this.name = name;
        this.role = role;
    }

    public boolean isMentor() {
        return this.role == UserRole.MENTOR;
    }

    public boolean isMentee() {
        return this.role == UserRole.MENTEE;
    }

    public void changeName(String name) {
        Assert.hasText(name, "Name must not be empty");

        this.name = name;
    }
}