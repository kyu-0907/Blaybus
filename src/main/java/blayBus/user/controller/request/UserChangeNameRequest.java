package blayBus.user.controller.request;

import jakarta.validation.constraints.NotBlank;

public record UserChangeNameRequest(
        @NotBlank String name
) {
}
