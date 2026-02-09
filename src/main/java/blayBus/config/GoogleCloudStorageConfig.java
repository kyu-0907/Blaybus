package blayBus.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.NoCredentials;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class GoogleCloudStorageConfig {

    @Value("${spring.cloud.gcp.storage.credentials.location:}")
    private Resource keyFile;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.storage.endpoint:}") // 에뮬레이터 주소 (없으면 빈값)
    private String endpoint;

    @Bean
    public Storage storage() throws IOException {
        StorageOptions.Builder builder = StorageOptions.newBuilder().setProjectId(projectId);

        if (endpoint != null && !endpoint.isEmpty()) {
            Storage storage = builder
                    .setHost(endpoint)
                    .setCredentials(NoCredentials.getInstance())
                    .build()
                    .getService();

            // [추가] 로컬 에뮬레이터일 경우 버킷이 없으면 생성
            String bucketName = "politic-bucket"; // 사용할 버킷 이름
            if (storage.get(bucketName) == null) {
                storage.create(BucketInfo.of(bucketName));
            }

            return storage;
        }

        return builder
                .setCredentials(GoogleCredentials.fromStream(keyFile.getInputStream()))
                .build()
                .getService();
    }
}
