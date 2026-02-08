package blayBus.material.application;

import blayBus.material.domain.Material;
import blayBus.material.domain.MaterialRepository;
import blayBus.material.domain.MaterialType;
import blayBus.task.application.FileStore;
import blayBus.task.domain.Task;
import blayBus.task.domain.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaterialService {
    private final TaskRepository taskRepository;
    private final MaterialRepository materialRepository;
    private final FileStore fileStore;

    @Transactional
    public void addMaterials(Long taskId, List<MultipartFile> files) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        List<Material> materials = files.stream()
                .filter(f -> f != null && !f.isEmpty())
                .map(f -> {
                    String url = fileStore.storeFile(f);
                    return Material.of(inferType(f), url, task);
                })
                .toList();

        materialRepository.saveAll(materials);
    }

    private MaterialType inferType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType.equalsIgnoreCase("application/pdf")) return MaterialType.PDF;
        return MaterialType.COLUMN; // fallback
    }
}
