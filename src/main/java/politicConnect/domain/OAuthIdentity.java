package politicConnect.domain;


import jakarta.persistence.Column;

public class OAuthIdentity extends AuthIdentity {

    @Column(name = "provider_user_id", nullable = false, length = 100)
    private String providerUserId;  // 소셜 플랫폼에서 받은 고유 ID (ex. Kakao ID, Google sub)

    @Column(length = 120)
    private String email;           // 동의받은 이메일 (선택)

    @Column(name = "display_name", length = 60)
    private String displayName;     // 프로필 이름 (선택)

    public OAuthIdentity(Provider provider, String providerUserId) {
        super();
        this.provider = provider;
        this.providerUserId = providerUserId;
    }



}
