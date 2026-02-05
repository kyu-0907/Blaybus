package blayBus.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {
    @MockitoBean
    private UserDetailsService userDetailsServiceMock;

    final String secret = "test-secret-key-which-is-at-least-32-bytes-long!!!";
    final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    @Test
    void expiredToken() {
        String userId = "123";
        Date now = new Date();
        Date issuedAt = new Date(now.getTime() - 3600_000);
        Date expiration = new Date(now.getTime() - 1000);

        String expiredToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key)
                .compact();

        JwtProvider jwtProvider = new JwtProvider(
                userDetailsServiceMock,
                secret,
                3600000L
        );

        boolean result = jwtProvider.validateToken(expiredToken);

        assertThat(result).isFalse();
    }

    @Test
    void validToken() {
        String userId = "123";
        Date now = new Date();
        Date issuedAt = new Date(now.getTime());
        Date expiration = new Date(now.getTime() + 3600_000);

        String validToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key)
                .compact();

        JwtProvider jwtProvider = new JwtProvider(
                userDetailsServiceMock,
                secret,
                3600000L
        );

        boolean result = jwtProvider.validateToken(validToken);

        assertThat(result).isTrue();
    }
}
