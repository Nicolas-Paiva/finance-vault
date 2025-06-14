package com.finance_vault.finance_vault.service.user;

import com.finance_vault.finance_vault.exception.UserNotFoundException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserEmailFromAuthentication(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
    }

}
