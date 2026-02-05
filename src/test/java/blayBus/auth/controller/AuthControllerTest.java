package blayBus.auth.controller;

import blayBus.auth.controller.request.LoginRequest;
import blayBus.auth.domain.AuthAccount;
import blayBus.auth.domain.AuthAccountRepository;
import blayBus.user.domain.User;
import blayBus.user.domain.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthAccountRepository authAccountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void loginSuccess() throws JsonProcessingException {
        User user = userRepository.save(User.createMentee("testName"));
        authAccountRepository.save(AuthAccount.of("loginId", passwordEncoder.encode("password"), user));
        em.flush();
        em.clear();

        LoginRequest request = new LoginRequest("loginId", "password");

        MvcTestResult result = mvcTester.post().uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .containsHeader(HttpHeaders.AUTHORIZATION)
                .bodyJson()
                .asString().isNotBlank();

        String authHeader = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        assertThat(authHeader).startsWith("Bearer ");
    }
}