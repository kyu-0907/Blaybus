package politicConnect.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import politicConnect.domain.Provider;
import politicConnect.domain.User;
import politicConnect.dto.SocialLoginResponse;
import politicConnect.dto.TokenDto;
import politicConnect.repository.UserRepository;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KakaoService {


    private final UserRepository userRepository;
    private final SocialLoginService socialLoginService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false; // let caller inspect all responses
            }
        });
    }


    @Transactional
    public SocialLoginResponse handleKakaoLogin(String code) {
        String accessToken = getAccessTokenFromKakao(code);
        String kakaoId = getKakaoId(accessToken);

        return userRepository.findByProviderAndProviderId(Provider.KAKAO,  kakaoId)
                .map(user -> {
                    TokenDto token = socialLoginService.generateTokenFor(user);
                    user.setRefreshToken(token.getRefreshToken());
                    return new SocialLoginResponse(true,Provider.KAKAO, kakaoId, token);
                })
                .orElseGet(() -> new SocialLoginResponse(false, Provider.KAKAO,kakaoId, null));
    }


    public String getAccessTokenFromKakao(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code"
                + "&client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&code=" + code;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println("카카오 응답 상태: " + response.getStatusCode());
        System.out.println("카카오 응답 바디: " + response.getBody());

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("카카오 토큰 요청 실패: " + response.getStatusCode());
        }

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode accessTokenNode = json.get("access_token");
            if (accessTokenNode == null) {
                throw new IllegalStateException("카카오 응답에 access_token 없음: " + response.getBody());
            }
            return accessTokenNode.asText();
        } catch (Exception e) {
            throw new IllegalStateException("카카오 토큰 파싱 실패", e);
        }
    }

    public String getKakaoId(String token) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println("사용자 정보 요청 토큰: " + token);
        System.out.println("카카오 사용자 정보 응답 바디: " + response.getBody());

        if (response.getBody() == null) {
            System.err.println("카카오 응답 바디가 null임. 이메일 조회 실패");
            return null; // 여기서 null 반환
        }

        try {
            JsonNode json = objectMapper.readTree(response.getBody());

            if (json.has("id")) {
                return "kakao_" + json.get("id").asText() + "@kakao-temp.local";
            }

            /* 1순위: 이메일
            JsonNode account = json.get("kakao_account");
            if (account != null && account.has("email")) {
                return account.get("email").asText();
            }

            // 2순위: 닉네임
            if (account != null && account.has("profile") && account.get("profile").has("nickname")) {
                return account.get("profile").get("nickname").asText() + "@kakao-temp.local";
            }

            // 3순위: ID라도 넘기기
            if (json.has("id")) {
                return "kakao_" + json.get("id").asText() + "@kakao-temp.local";
            }

            */

            return null;
        } catch (Exception e) {
            System.err.println("카카오 사용자 정보 파싱 실패: " + e.getMessage());
            return null;
        }
    }

    public String getAccessTokenDebug(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code"
                + "&client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&code=" + code;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println("DEBUG용 응답 상태: " + response.getStatusCode());
        System.out.println("DEBUG용 응답 바디: " + response.getBody());

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("access_token").asText();  // null 가능성 있음
        } catch (Exception e) {
            throw new IllegalStateException("access_token 파싱 실패", e);
        }
    }


}






