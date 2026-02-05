package blayBus.user.application;

import blayBus.common.exception.UserNotFoundException;
import blayBus.user.application.request.UserCreateCommand;
import blayBus.user.domain.User;
import blayBus.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserFactory userFactory;
    private final UserRepository userRepository;

    @Transactional
    public User createUser(UserCreateCommand userCreateCommand) {
        User user = userFactory.create(userCreateCommand.role(), userCreateCommand.name());

        return userRepository.save(user);
    }

    public User findUserById(Long userId) {
        return findUser(userId);
    }

    @Transactional
    public User changeUserName(Long userId, String newName) {
        User user = findUserById(userId);
        user.changeName(newName);

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findUser(userId);

        userRepository.delete(user);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
