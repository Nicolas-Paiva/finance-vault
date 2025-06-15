package com.finance_vault.finance_vault.service.user;

import com.finance_vault.finance_vault.exception.UserNotFoundException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserFromEmail(String email) {
        return userRepository.findByEmail(email);
    }



}
