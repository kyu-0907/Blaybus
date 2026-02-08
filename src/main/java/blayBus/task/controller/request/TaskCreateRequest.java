package blayBus.task.controller.request;

import blayBus.task.application.request.TaskCreateCommand;
import blayBus.task.domain.Subject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskCreateRequest(
        @NotNull Long assigneeId,
        @NotNull LocalDate date,
        @NotBlank String title,
        String goal,
        @NotNull Subject subject
) {
    public TaskCreateCommand toCommand() {
        return new TaskCreateCommand(assigneeId, date, title, goal, subject);
    }
}
