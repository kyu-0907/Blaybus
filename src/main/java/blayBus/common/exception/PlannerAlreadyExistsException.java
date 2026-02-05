package blayBus.common.exception;

public class PlannerAlreadyExistsException extends RuntimeException {
    public PlannerAlreadyExistsException() {
        super("Planner already exists for given date");
    }
}
