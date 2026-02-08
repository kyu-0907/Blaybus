package blayBus.task.application.request;

import blayBus.task.domain.Subject;

import java.time.LocalDate;

public record TaskCreateCommand(
        Long assigneeId,
        LocalDate date,
        String title,
        String goal,
        Subject subject
) {
}
