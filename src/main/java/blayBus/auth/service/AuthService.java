package blayBus.auth.service;

import blayBus.auth.JwtProvider;
import blayBus.auth.domain.AuthAccount;
import blayBus.auth.domain.AuthAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AuthAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public String login(String loginId, String password) {
        AuthAccount account = accountRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadCredentialsException("Invalid loginId or password"));

        if (!passwordEncoder.matches(password, account.getEncryptedPassword())) {
            throw new BadCredentialsException("Invalid loginId or password");
        }

        return jwtProvider.createToken(account.getUserId());
    }
}
