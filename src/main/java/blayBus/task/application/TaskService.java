package blayBus.task.application;

import blayBus.common.exception.UserNotFoundException;
import blayBus.planner.domain.Planner;
import blayBus.planner.domain.PlannerRepository;
import blayBus.task.application.request.TaskCreateCommand;
import blayBus.task.domain.Task;
import blayBus.task.domain.TaskInfo;
import blayBus.task.domain.TaskRepository;
import blayBus.user.domain.User;
import blayBus.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    private final TaskRepository taskRepository;
    private final FileStore fileStore;

    @Transactional
    public Task createTask(Long authorId, TaskCreateCommand command, MultipartFile file) throws IOException {
        User author = findUser(authorId);
        User assignee = findUser(command.assigneeId());

        Planner planner = findOrCreatePlanner(command, assignee);

        String filename = fileStore.storeFile(file);

        Task task = Task.create(TaskInfo.of(
                planner,
                command,
                filename,
                author
        ));

        return taskRepository.save(task);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Planner findOrCreatePlanner(TaskCreateCommand command, User assignee) {
        return plannerRepository.findByDateAndUser(command.date(), assignee)
                .orElseGet(() -> Planner.create(command.date(), assignee));
    }
}
