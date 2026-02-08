package blayBus.task.controller.request;

import blayBus.task.domain.Subject;
import blayBus.task.domain.TaskStatus;

public record TaskUpdateRequest(
        String title,
        String goal,
        Subject subject,
        TaskStatus status
) {
}
