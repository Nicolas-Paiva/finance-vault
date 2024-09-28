package com.nicolaspaiva.finance_vault.auth.controller;

import com.nicolaspaiva.finance_vault.auth.dto.RefreshTokenRequest;
import com.nicolaspaiva.finance_vault.auth.dto.SignInRequest;
import com.nicolaspaiva.finance_vault.auth.dto.SignUpRequest;
import com.nicolaspaiva.finance_vault.auth.dto.SignUpResponse;
import com.nicolaspaiva.finance_vault.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling
 * authentication (sign up and sign in)
 *
 * All requests are allowed, that is,
 * no JWT is required for accessing
 * the endpoints
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint used for signing a user up.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest){

        SignUpResponse response = authenticationService.signUp(signUpRequest);

        if(response.isCreated()){
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }


    /**
     * Endpoint dedicated to providing user sign in
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.generateRefreshToken(refreshTokenRequest));
    }

    @PostMapping("/activate-account")
    public String activateAccount(@RequestBody String token){
        return authenticationService.verifyToken(token);
    }

}
