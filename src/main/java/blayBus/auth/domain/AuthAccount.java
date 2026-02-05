package blayBus.auth.domain;

import blayBus.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginId;

    private String encryptedPassword;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public static AuthAccount of(String loginId, String encryptedPassword, User user) {
        AuthAccount account = new AuthAccount();

        account.loginId = loginId;
        account.encryptedPassword = encryptedPassword;
        account.user = user;

        return account;
    }

    public String getUserId() {
        return String.valueOf(user.getId());
    }

    public String getRole() {
        return "ROLE_" + user.getRole().name();
    }
}
