package blayBus.user.controller.request;

import blayBus.user.application.request.UserCreateCommand;
import blayBus.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        @NotNull UserRole role,
        @NotBlank String name
) {
    public UserCreateCommand toCommand() {
        return UserCreateCommand.of(role, name);
    }
}
