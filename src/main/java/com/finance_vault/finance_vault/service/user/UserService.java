package com.finance_vault.finance_vault.service.user;

import com.finance_vault.finance_vault.dto.auth.RegistrationResponse;
import com.finance_vault.finance_vault.dto.auth.LoginSuccessResponse;
import com.finance_vault.finance_vault.dto.user.UserDTO;
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
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    private final JWTService jwtService;


    public RegistrationResponse register(UserDTO userDTO) {

        System.out.println(userDTO.getEmail());

        // Checks whether the username already exists
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw InvalidRegistrationException.userEmailAlreadyExists();
        }

        // Checks whether the password is valid
        if (!Utils.isPasswordValid(userDTO.getPassword())) {
            throw InvalidRegistrationException.invalidPassword();
        }

        // Proceeds to register the user
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(UserDTO.toUser(userDTO));

        return RegistrationResponse.success(userDTO);
    }


    /**
     * Logs the user in using the AuthenticationManager
     * and issuing a JWT to the user.
     *
     * If the user fails to authenticate, an InvalidUserNameOrPassword
     * exception is thrown, which causes a LoginErrorResponse to be sent
     * to the user.
     */
    public LoginSuccessResponse login(UserDTO userDTO) {
        Authentication authentication;

        User user = UserDTO.toUser(userDTO);

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
