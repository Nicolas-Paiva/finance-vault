package com.finance_vault.finance_vault.service.user;

import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.model.user.UserPrincipal;
import com.finance_vault.finance_vault.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Defines how the user is retrieved from
 * the datasource, which in this case,
 * comes from PostgreSQL
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrincipal(user);
    }

}
