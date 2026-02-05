package blayBus.auth.service;

import blayBus.auth.domain.AuthAccount;
import blayBus.auth.domain.AuthAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthAccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        long userId = Long.parseLong(username);

        AuthAccount account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("AuthAccount not found for userId: " + userId));

        return org.springframework.security.core.userdetails.User.builder()
                .username(account.getUserId())
                .password("")
                .roles(account.getRole())
                .build();
    }
}
