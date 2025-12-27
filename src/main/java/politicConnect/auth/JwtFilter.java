package politicConnect.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class JwtFilter extends OncePerRequestFilter {

        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "Bearer";

        private final JwtProvider jwtProvider;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

            String path = request.getRequestURI();

            // [수정] 스웨거 관련 경로도 필터 로직을 건너뛰도록 추가
            if (path.startsWith("/oauth2/") ||
                    path.startsWith("/auth/") ||
                    path.startsWith("/swagger-ui") ||
                    path.startsWith("/v3/api-docs") ||
                    path.startsWith("/swagger-resources") ||
                    path.startsWith("/favicon.ico")) {

                filterChain.doFilter(request, response);
                return; // 여기서 메서드 종료! 밑에 토큰 검사 로직 실행 안 됨
            }

            // --- 기존 로직 ---
            String jwt = resolveToken(request);

            // validateToken 으로 토큰 유효성 검사
            if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                Authentication authentication = jwtProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        }

        // Request Header 에서 토큰 정보를 꺼내오기
        private String resolveToken(HttpServletRequest request) {
            String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
                return bearerToken.split(" ")[1].trim();
            }
            return null;
        }
}