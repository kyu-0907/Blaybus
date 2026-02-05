package blayBus.planner.application.request;

import java.time.LocalDate;

public record PlannerCreateCommand(
        LocalDate date,
        long userId
) {
}
