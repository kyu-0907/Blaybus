package politicConnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "카카오 로그인 응답 DTO")
public class KaKaoLoginResponse {
    private boolean isSignedUp;
    private String id;
    private TokenDto tokenDto;
}
