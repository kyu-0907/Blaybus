package blayBus.task.controller;

import blayBus.task.application.TaskService;
import blayBus.task.controller.request.TaskCreateRequest;
import blayBus.task.controller.response.TaskCreateResponse;
import blayBus.task.controller.response.TaskResponse;
import blayBus.task.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TaskCreateResponse> createTask(
            @RequestParam Long authorId,
            @RequestPart TaskCreateRequest request,
            @RequestPart List<MultipartFile> files
    ) {
        Task task = taskService.createTask(authorId, request.toCommand(), files);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TaskCreateResponse(task.getId()));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> findTask(@PathVariable Long taskId) {
        Task task = taskService.findTask(taskId);

        return ResponseEntity.ok(TaskResponse.from(task));
    }
}
