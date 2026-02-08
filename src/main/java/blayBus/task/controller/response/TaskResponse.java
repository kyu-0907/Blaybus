package blayBus.task.controller.response;

import blayBus.task.domain.Task;

public record TaskResponse(
        Long taskId,
        String title,
        String goal,
        String subject,
        String status,
        String filename
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getGoal(),
                task.getSubject().name(),
                task.getStatus().name(),
                task.getFilename()
        );
    }
}
