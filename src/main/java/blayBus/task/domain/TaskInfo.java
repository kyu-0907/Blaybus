package blayBus.task.domain;

import blayBus.planner.domain.Planner;
import blayBus.task.application.request.TaskCreateCommand;
import blayBus.user.domain.User;

public record TaskInfo(
        Planner planner,
        String title,
        String goal,
        Subject subject,
        String filename,
        User author
) {
    public static TaskInfo of(
            Planner planner,
            TaskCreateCommand taskCreateCommand,
            String filename,
            User author
    ) {
        return new TaskInfo(
                planner,
                taskCreateCommand.title(),
                taskCreateCommand.goal(),
                taskCreateCommand.subject(),
                filename,
                author
        );
    }
}
