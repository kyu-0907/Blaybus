package politicConnect.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"provider", "provider_id"})
        }
)

@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String localLoginId;

    @Column
    private String localLoginPassword;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private RegionCity regionCity;

    //로컬 로그인 시 직접입력, 소셜로그인 시 제공되는 메일 자동입₩
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column
    private String providerId;

    /** JWT Refresh Token (optional) */
    @Column(length = 512)
    private String refreshToken;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

}
