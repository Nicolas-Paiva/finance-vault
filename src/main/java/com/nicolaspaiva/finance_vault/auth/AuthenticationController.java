package com.nicolaspaiva.finance_vault.auth;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     *<br>
     *
     * Success Response:
     * <pre>
     * {
     *     "created": true,
     *     "message": "User created"
     * }
     * </pre>
     *
     * If the request is not valid, a failed response
     * is sent<br>
     * Failed response:
     * <pre>
     * {
     *     "password": "Password must contain at least one special character",
     *     "email": "Invalid email address"
     * }
     * </pre>
     * @param signUpRequest a valid SignUpRequest object must
     *                      be sent by the client in order
     *                      to have the user signed in
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest){

        if(authenticationService.signUp(signUpRequest).isCreated()){
            return new ResponseEntity<>(new SignUpResponse().success(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new SignUpResponse().failed(), HttpStatus.BAD_REQUEST);
    }


    /**
     * Endpoint dedicated to providing user sign in.
     * If
     * @param signInequest the SignInRequest object sent by the client
     * in order to perform sign in
     * <br>
     * <br>
     * Success Response:
     * <pre>
     * {
     *     "token": "...",
     *     "refreshToken": "..."
     * }
     * </pre>
     * If authentication fails, the following response is sent to the client:
     * <br>
     * <br>
     * Failed Authentication Response:
     * <pre>
     *     {
     *     "error": "Invalid username or password"
     *     }
     * </pre>
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInequest){
        return ResponseEntity.ok(authenticationService.signIn(signInequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.generateRefreshToken(refreshTokenRequest));
    }

}
