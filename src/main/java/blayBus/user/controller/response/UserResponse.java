package blayBus.user.controller.response;

import blayBus.user.domain.User;
import blayBus.user.domain.UserRole;

public record UserResponse(
        Long id,
        UserRole role,
        String name
) {
    static public UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getRole(),
                user.getName()
        );
    }
}
