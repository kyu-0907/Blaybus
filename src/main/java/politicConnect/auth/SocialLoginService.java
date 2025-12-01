package politicConnect.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import politicConnect.domain.Provider;
import politicConnect.domain.User;
import politicConnect.dto.SocialLoginResponse;
import politicConnect.dto.SocialSignUpRequest;
import politicConnect.dto.TokenDto;
import politicConnect.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenDto completeSignup(SocialSignUpRequest req) {

        User user = User.builder()
                .regionCity(req.getRegionCity())
                .nickName(req.getNickname())
                .email(req.getEmail())
                .build();

        TokenDto token = generateTokenFor(user);
        user.setRefreshToken(token.getRefreshToken());
        userRepository.save(user);

        return token;
    }

    public TokenDto generateTokenFor(User user) { //jwt 토큰이 아니고 usernamepasswordauthenticaitontken
        return jwtProvider.generateTokenDto(
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
                )
        );
    }

    public TokenDto refreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        TokenDto token = generateTokenFor(user);
        user.setRefreshToken(token.getRefreshToken());
        userRepository.save(user);

        return token;
    }


    }

