package politicConnect.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import politicConnect.domain.Role;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException {

        // 1. 유저 정보 꺼내기
        PrincipalDetails customUser = (PrincipalDetails) authentication.getPrincipal();
        Role role = customUser.getUser().getRole();
        String email = customUser.getUser().getEmail(); // null일 수 있음

        String accessToken = jwtProvider.generateTokenDto(authentication);

        String targetUrl;
        String baseUrl = "";//프론트 주소

        if (role == Role.GUEST) {
            // [신규 유저] -> 회원가입 페이지로 이동 (이메일 포함!)
            targetUrl = UriComponentsBuilder.fromUriString(baseUrl + "/social/signup")
                    .queryParam("accessToken", accessToken)
                    .queryParam("isNewUser", true)
                    .queryParam("email", email) // 👈 여기에 이메일 추가
                    .encode(StandardCharsets.UTF_8) // 한글/특수문자 깨짐 방지
                    .build().toUriString();
        } else {
            // [기존 유저] -> 메인 페이지로 이동
            targetUrl = UriComponentsBuilder.fromUriString(baseUrl + "/oauth/callback")  //여기 메인페이지 url로 변경
                    .queryParam("accessToken", accessToken)
                    .build().toUriString();
        }

        // 4. 리디렉트 수행
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }
}
