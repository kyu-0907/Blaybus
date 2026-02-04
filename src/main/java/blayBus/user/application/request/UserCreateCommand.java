package blayBus.user.application.request;

import blayBus.user.domain.UserRole;

public record UserCreateCommand(
        UserRole role,
        String name
) {

    public static UserCreateCommand of(UserRole role, String name) {
        return new UserCreateCommand(role, name);
    }
}
