package com.nicolaspaiva.finance_vault.auth.service;

import com.nicolaspaiva.finance_vault.auth.dto.*;
import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.service.BankAccountService;
import com.nicolaspaiva.finance_vault.security.jwt.JwtService;
import com.nicolaspaiva.finance_vault.user.entity.Role;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuthenticationManager authManager;

    private final JwtService jwtService;

    private final BankAccountService bankAccountService;


    /**
     * Registers a new user.<br>
     *
     * Password validation is
     * provided by Spring Validation.<br>
     *
     */
    public SignUpResponse signUp(SignUpRequest signUpRequest){

        // Checks whether the provided email already exists
        if(emailAlreadyExists(signUpRequest.getEmail())){
            return new SignUpResponse(false, "Email already exists");
        }

        try{

            UserEntity user = createUserEntity(signUpRequest);

            // Creates the user bank account
            BankAccountEntity bankAccount = bankAccountService.createBankAccount(user);

            // When the user is saved, the bank account is saved due to cascading
            user.setAccount(bankAccount);

            userRepository.save(user);


        } catch (Exception e){
            e.getLocalizedMessage();
            return SignUpResponse.failed();
        }

        return SignUpResponse.success();
    }

    /**
     * Creates a user entity based on a SignUpRequest
     */
    private UserEntity createUserEntity(SignUpRequest signUpRequest){
        return UserEntity.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode((signUpRequest.getPassword())))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }


    /**
     * Checks whether the provided email exists in the
     * database
     */
    public boolean emailAlreadyExists(String email){
        Optional<UserEntity> user = userRepository.findByEmail(email);

        return user.isPresent();
    }


    /**
     * Sends a JWT to the user in case
     * authentication is successful.
     *
     * @return a JWT token if the user is authenticated,
     * throws an exception if the user is not found.
     *
     * When the exception is thrown, it is handled by
     * the Exception Handler method, which returns
     *
     */
    public JwtAuthenticationResponse signIn(SignInRequest request){
        authManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * Generates a JWT refresh token
     */
    public JwtAuthenticationResponse generateRefreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            return JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .refreshToken(refreshTokenRequest.getToken())
                    .build();
        }

        return null;
    }
}
