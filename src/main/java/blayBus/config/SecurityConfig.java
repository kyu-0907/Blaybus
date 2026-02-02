package blayBus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {





        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    // 요청 권한 설정
                    .authorizeHttpRequests(auth -> auth
                                    // 모든 요청(/**)을 허용(permitAll)하겠다는 설정입니다.
                                    .requestMatchers("/**").permitAll()
                            .anyRequest().authenticated()
                    )

                    // CSRF 완전히 비활성화
                    .csrf(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable);


            return http.build();
        }
    }