package blayBus.common.exception;

public class PlannerNotFoundException extends RuntimeException {
    public PlannerNotFoundException() {
        super("Planner not found");
    }
}
