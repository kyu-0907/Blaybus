package blayBus.user.controller;

import blayBus.user.controller.request.UserChangeNameRequest;
import blayBus.user.controller.request.UserCreateRequest;
import blayBus.user.domain.User;
import blayBus.user.domain.UserRepository;
import blayBus.user.domain.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvcTester mvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager em;

    @DisplayName("사용자 생성 요청이 성공하면 201과 생성된 사용자 정보를 반환한다")
    @Test
    void createUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest(UserRole.MENTEE, "testUser");

        MvcTestResult result = mvcTester.post().uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .hasPathSatisfying("$.id", id -> assertThat(id).isNotNull())
                .hasPathSatisfying("$.role", role -> assertThat(role).isEqualTo(UserRole.MENTEE.name()))
                .hasPathSatisfying("$.name", name -> assertThat(name).isEqualTo("testUser"));
    }

    @DisplayName("생성한 사용자를 조회할 수 있다")
    @Test
    void findUser() {
        User user = userRepository.save(createTestUser());

        MvcTestResult result = mvcTester.get().uri("/api/v1/users/{userId}", user.getId())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .hasPathSatisfying("$.role", role -> assertThat(role).isEqualTo(user.getRole().name()))
                .hasPathSatisfying("$.name", name -> assertThat(name).isEqualTo(user.getName()))
                .extractingPath("$.id").convertTo(Long.class).isEqualTo(user.getId());
    }

    @DisplayName("사용자 이름을 변경할 수 있다")
    @Test
    void changeName() throws Exception {
        User user = userRepository.save(createTestUser());
        UserChangeNameRequest request = new UserChangeNameRequest("newName");

        MvcTestResult result = mvcTester.patch().uri("/api/v1/users/{userId}/name", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .hasPathSatisfying("$.id", id -> assertThat(id).isNotNull())
                .hasPathSatisfying("$.role", role -> assertThat(role).isEqualTo(UserRole.MENTEE.name()))
                .hasPathSatisfying("$.name", name -> assertThat(name).isEqualTo("newName"));
    }

    @DisplayName("사용자를 삭제할 수 있다")
    @Test
    void deleteUser() {
        User user = userRepository.save(createTestUser());

        mvcTester.delete().uri("/api/v1/users/{userId}", user.getId())
                .exchange();
        em.flush();
        em.clear();

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    User createTestUser() {
        return User.createMentee("test");
    }
}