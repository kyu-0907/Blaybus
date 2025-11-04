package politicConnect.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "local_identities",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_local_login_id", columnNames = "login_id")
        }
)
public class LocalIdentity extends AuthIdentity {

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;  // 사용자가 입력한 로그인 아이디

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash; // 암호화된 비밀번호 (BCrypt 등)

    public LocalIdentity(String loginId, String passwordHash) {
        super();
        Provider ProviderType;
        this.provider = Provider.LOCAL;
        this.loginId = loginId;
        this.passwordHash = passwordHash;
    }



}
