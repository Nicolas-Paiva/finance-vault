package com.nicolaspaiva.finance_vault.auth.service;

import com.nicolaspaiva.finance_vault.auth.dto.*;

import java.util.Optional;

/**
 * Service responsible for
 * sign in and sign up functionality
 */
public interface AuthenticationService {

    // Signs a user up, also creating their bank account
    SignUpResponse signUp(SignUpRequest request);

    // Signs a user in
    JwtAuthenticationResponse signIn(SignInRequest request);

    // Generates a refresh token
    JwtAuthenticationResponse generateRefreshToken(RefreshTokenRequest refreshTokenRequest);

    // TODO: Improve responses
    String verifyToken(String token);
}
