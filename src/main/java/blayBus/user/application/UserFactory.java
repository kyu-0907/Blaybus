package blayBus.user.application;

import blayBus.user.domain.User;
import blayBus.user.domain.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public User create(UserRole role, String name) {
        return switch (role) {
            case MENTOR -> User.createMentor(name);
            case MENTEE -> User.createMentee(name);
        };
    }
}
