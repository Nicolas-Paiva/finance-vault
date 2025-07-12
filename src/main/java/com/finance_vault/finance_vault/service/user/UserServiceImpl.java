package com.finance_vault.finance_vault.service.user;

import com.finance_vault.finance_vault.exception.InvalidProfileChangeException;
import com.finance_vault.finance_vault.exception.InvalidRegistrationException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.UserRepository;
import com.finance_vault.finance_vault.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public Optional<User> getUserFromEmail(String email) {
        return userRepository.findByEmail(email);
    }


    /**
     * Changes a user's email only if the provided email
     * has a valid format and is not currently linked
     * to an account in the database.
     */
    @Override
    public void changeUserEmail(User user, String newEmail) {

        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw InvalidProfileChangeException.emailAlreadyExists();
        }

        if (!Utils.isEmailFormatValid(newEmail)) {
            throw InvalidProfileChangeException.invalidEmail();
        }

        user.setEmail(newEmail);
        userRepository.save(user);
    }


    /**
     * Changes a user's password only if the provided email is
     * valid
     */
    @Override
    public void changeUserPassword(User user, String newPassword) {
        if (!Utils.isPasswordValid(newPassword)) {
            throw InvalidProfileChangeException.invalidPassword();
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    @Override
    public void changeUserName(User user, String newName, String newLastName) {
        user.setName(newName);
        user.setLastName(newLastName);
        userRepository.save(user);
    }


}
