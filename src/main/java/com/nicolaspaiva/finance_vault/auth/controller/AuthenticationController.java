package com.nicolaspaiva.finance_vault.auth.controller;

import com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto.ConfirmationTokenRequest;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto.ConfirmationTokenResponse;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.service.ConfirmationTokenService;
import com.nicolaspaiva.finance_vault.auth.dto.*;
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

    private final ConfirmationTokenService confirmationTokenService;

    /**
     * Endpoint used for signing a user up.
     *
     * Upon signing up, a confirmation email
     * is sent to the user in order to activate
     * their account
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
        SignInResponse signInResponse = authenticationService.signIn(signInRequest);

        if(signInResponse.isSuccess()){
            return new ResponseEntity<>(signInResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(signInResponse, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/activate-account")
    public ResponseEntity<?> activateAccount(@RequestBody ConfirmationTokenRequest token){
        ConfirmationTokenResponse confirmationTokenResponse =
                confirmationTokenService.verifyToken(token.getConfirmationToken());

        if(confirmationTokenResponse.isSuccess()){
            return ResponseEntity.ok(confirmationTokenResponse);
        }

        return new ResponseEntity<>(confirmationTokenResponse, HttpStatus.BAD_REQUEST);
    }

}
