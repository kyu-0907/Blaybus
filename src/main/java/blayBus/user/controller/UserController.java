package blayBus.user.controller;

import blayBus.user.application.UserService;
import blayBus.user.controller.request.UserChangeNameRequest;
import blayBus.user.controller.request.UserCreateRequest;
import blayBus.user.controller.response.UserResponse;
import blayBus.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(request.toCommand());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> find(@PathVariable Long userId) {
        User user = userService.findUserById(userId);

        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PatchMapping("/{userId}/name")
    public ResponseEntity<UserResponse> changeName(
            @PathVariable Long userId,
            @Valid @RequestBody UserChangeNameRequest request
    ) {
        User user = userService.changeUserName(userId, request.name());

        return ResponseEntity.ok(UserResponse.from(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
