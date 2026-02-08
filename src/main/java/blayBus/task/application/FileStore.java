package blayBus.task.application;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStore {
    String storeFile(MultipartFile file);
}
