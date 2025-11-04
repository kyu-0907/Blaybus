package politicConnect.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import politicConnect.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper= new ObjectMapper();

    @Value("${kakao.client-id")
    private  String kakaoClientId;

    @Value("${kakao.redirect-uri")
    private  String kakaoRedirectUri;







}
