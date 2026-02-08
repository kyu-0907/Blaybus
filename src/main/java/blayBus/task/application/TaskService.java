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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Task createTask(Long authorId, TaskCreateCommand command, List<MultipartFile> files) {
        User author = findUser(authorId);
        User assignee = findUser(command.assigneeId());

        Planner planner = findOrCreatePlanner(command, assignee);

        Task task = taskRepository.save(Task.create(TaskInfo.of(planner, command, author)));

        if (files != null && !files.isEmpty()) {
            eventPublisher.publishEvent(new TaskCreateEvent(task.getId(), files));
        }

        return task;
    }

    public Task findTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
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
