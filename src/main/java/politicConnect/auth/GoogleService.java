package politicConnect.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import politicConnect.domain.Provider;
import politicConnect.dto.SocialLoginResponse;
import politicConnect.dto.TokenDto;
import politicConnect.repository.UserRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleService {


    private final UserRepository userRepository;
    private final SocialLoginService socialLoginService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${google.client-id}")
    private String googleClientId;

    @Value("$google.redirect-uri}")
    private String googleRedirectUri;

    @Value("google.client-secret")
    private String googleClientSecret;

    @Transactional
    public SocialLoginResponse handleGoogleLogin(String code){

        String accessToken = getAccessTokenFromGoogle(code);
        String googleId = getGoogleId(accessToken);

        return userRepository.findByProviderAndProviderId(Provider.GOOGLE,  googleId)
                .map(user -> {
                    TokenDto token = socialLoginService.generateTokenFor(user);
                    user.setRefreshToken(token.getRefreshToken());
                    return new SocialLoginResponse(true,Provider.GOOGLE, googleId, token);
                })
                .orElseGet(() -> new SocialLoginResponse(false, Provider.GOOGLE,googleId, null));
    }



    public String getGoogleId(String accessToken){

        String url = "https://openidconnect.googleapis.com/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);  // Authorization: Bearer {accessToken}

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("sub")) {
            throw new IllegalStateException("구글 userinfo 조회 실패: sub 없음");
        }

        // 🔥 구글 고유 유저 ID (social id)
        return (String) body.get("sub");
    }




    public String getAccessTokenFromGoogle(String code){

        String url = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("redirect_uri", googleRedirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        // 응답을 Map으로 받아 access_token만 추출
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );

        // 응답이 null이거나 access_token 없으면 에러 처리
        Map<String, Object> resBody = response.getBody();
        if (resBody == null || !resBody.containsKey("access_token")) {
            throw new IllegalStateException("구글 access_token 발급 실패");
        }

        // 🔥 access_token만 반환
        return (String) resBody.get("access_token");


    }




}
