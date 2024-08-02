package com.nicolaspaiva.finance_vault.auth.service;

import com.nicolaspaiva.finance_vault.auth.dto.*;
import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.service.BankAccountService;
import com.nicolaspaiva.finance_vault.security.jwt.JwtService;
import com.nicolaspaiva.finance_vault.user.entity.Role;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import com.nicolaspaiva.finance_vault.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * The user's bank account
     * is created during the process
     * @param signUpRequest the request received by
     * the signup API
     * @return a SignUpResponse, containing a message as
     * well as a created status (true or false)
     */
    public SignUpResponse signUp(SignUpRequest signUpRequest){

        UserEntity user;

        try{

            // Checks whether the provided email already exists
            if(emailAlreadyExists(signUpRequest.getEmail())){
                return new SignUpResponse(false, "Email already exists");
            }


            // Creates the user entity
             user = UserEntity.builder()
                    .firstName(signUpRequest.getFirstName())
                    .lastName(signUpRequest.getLastName())
                    .email(signUpRequest.getEmail())
                    .password(passwordEncoder.encode((signUpRequest.getPassword())))
                    .role(Role.USER)
                    .build();

            // Creates the user bank account
            BankAccountEntity bankAccount = bankAccountService.createBankAccount(user);

            user.setAccount(bankAccount);

            userRepository.save(user);


        } catch (Exception e){
            e.getLocalizedMessage();
            return new SignUpResponse().failed();
        }

        return new SignUpResponse().success();
    }


    /**
     * Checks whether the provided email exists in the
     * database
     * @param email
     * @return true if the email exists, false if it does not
     */
    public boolean emailAlreadyExists(String email){
        Optional<UserEntity> user = userRepository.findByEmail(email);

        return user.isPresent();
    }


    /**
     * Sends a JWT to the user in case
     * authentication is successful
     * @param request the sign in request
     * @return a JWT token if the user is authenticated,
     * throws an exception if the user is not found.
     *
     * When the exception is thrown, it is handled by
     * the Exception Handler method, which returns
     * the object:
     *
     * <br>
     * <pre>
     * {
     *     "error": "Invalid username or password"
     * }
     * </pre>
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
     * @param refreshTokenRequest
     * @return a JWT refresh token
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
