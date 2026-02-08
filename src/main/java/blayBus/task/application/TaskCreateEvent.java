package blayBus.task.application;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record TaskCreateEvent(
        Long taskId,
        List<MultipartFile> files
) {
}
