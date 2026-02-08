package blayBus.task.infrastructure;

import blayBus.task.application.FileStore;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GcsFileStore implements FileStore {
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String storeFileName = createStoreFileName(file.getOriginalFilename());

        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, storeFileName)
                    .setContentType(file.getContentType())
                    .build();

            storage.create(blobInfo, file.getInputStream());

            return "https://storage.googleapis.com/" + bucketName + "/" + storeFileName;

        } catch (IOException e) {
            throw new RuntimeException("GCS 파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
