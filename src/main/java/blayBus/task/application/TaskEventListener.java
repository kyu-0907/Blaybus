package blayBus.task.application;

import blayBus.material.application.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TaskEventListener {
    private final MaterialService materialService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTaskCreateEvent(TaskCreateEvent event) {
        materialService.addMaterials(event.taskId(), event.files());
    }
}
