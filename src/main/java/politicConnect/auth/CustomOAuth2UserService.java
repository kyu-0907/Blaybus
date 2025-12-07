package politicConnect.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import politicConnect.domain.Provider;
import politicConnect.domain.Role;
import politicConnect.domain.User;
import politicConnect.repository.RefreshTokenRepository;
import politicConnect.repository.UserRepository;
    import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 소셜 유저 정보 가져오기 (라이브러리 사용)
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. provider 판별 (google, github, kakao, naver)
        String provider = userRequest.getClientRegistration().getRegistrationId();

        Provider socialProvider = Provider.valueOf(provider);


        // 3. providerId 추출 (소셜마다 ID key가 다름을 해결)
        String providerId = extractProviderId(oAuth2User, provider);

        // 4. DB 조회 (provider + providerId)
        User user = userRepository.findByProviderAndProviderId(socialProvider, providerId)
                .orElse(null);

        // 5. 신규 유저라면 저장 (GUEST 권한)
        if (user == null) {
            user = User.builder()
                    .email(oAuth2User.getAttribute("email")) // 이메일이 없을 수도 있음(null 체크 필요시 로직 추가)
                    .provider(socialProvider)
                    .providerId(providerId)
                    .role(Role.GUEST) // 👈 신규 가입자는 GUEST
                    .build();
            userRepository.save(user);
        }

        // 6. Principal 반환 (SuccessHandler로 넘어감)
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    // 소셜별 ID 추출기
    private String extractProviderId(OAuth2User oAuth2User, String provider) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (provider.equals("google")) {
            return (String) attributes.get("sub");
        } else if (provider.equals("github")) {
            return String.valueOf(attributes.get("id")); // Integer -> String
        } else if (provider.equals("kakao")) {
            return String.valueOf(attributes.get("id")); // Long -> String
        } else if (provider.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return (String) response.get("id");
        }
        throw new OAuth2AuthenticationException("Unsupported Provider: " + provider);
    }


}
