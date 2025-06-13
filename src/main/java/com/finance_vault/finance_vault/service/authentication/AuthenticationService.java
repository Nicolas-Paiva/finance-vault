package com.finance_vault.finance_vault.service.authentication;

import com.finance_vault.finance_vault.dto.auth.RegistrationResponse;
import com.finance_vault.finance_vault.dto.auth.LoginSuccessResponse;
import com.finance_vault.finance_vault.dto.user.UserRegistrationRequest;
import com.finance_vault.finance_vault.exception.InvalidRegistrationException;
import com.finance_vault.finance_vault.exception.InvalidEmailOrPassword;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.user.UserRepository;
import com.finance_vault.finance_vault.security.jwt.JWTService;
import com.finance_vault.finance_vault.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service related to user registration
 * and login
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    private final JWTService jwtService;


    public RegistrationResponse register(UserRegistrationRequest userRegistrationRequest) {

        // Checks whether the username already exists
        if (userRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent()) {
            throw InvalidRegistrationException.userEmailAlreadyExists();
        }

        // Checks whether the password is valid
        if (!Utils.isPasswordValid(userRegistrationRequest.getPassword())) {
            throw InvalidRegistrationException.invalidPassword();
        }

        // Proceeds to register the user
        userRegistrationRequest.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        userRepository.save(UserRegistrationRequest.toUser(userRegistrationRequest));

        return RegistrationResponse.success(userRegistrationRequest);
    }


    /**
     * Logs the user in using the AuthenticationManager,
     * issuing a JWT to the user.
     *
     * If the user fails to authenticate, an InvalidUserNameOrPassword
     * exception is thrown, which causes a LoginErrorResponse to be sent
     * to the user.
     */
    public LoginSuccessResponse login(UserRegistrationRequest userRegistrationRequest) {
        Authentication authentication;

        User user = UserRegistrationRequest.toUser(userRegistrationRequest);

        // Tries to authenticate the user
        try {
             authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken
                                    (user.getEmail(), user.getPassword()));
        } catch(Exception e) {
            throw new InvalidEmailOrPassword();
        }

        if (authentication.isAuthenticated()) {
            String jwt = jwtService.generateToken(user.getEmail());
            return new LoginSuccessResponse(jwt);
        }

        throw new InvalidEmailOrPassword();
    }

}
