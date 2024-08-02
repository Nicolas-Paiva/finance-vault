package com.nicolaspaiva.finance_vault.auth.service;

import com.nicolaspaiva.finance_vault.auth.dto.*;

/**
 * The authentication service is responsible for
 * providing functionality regarding user authentication,
 * including sign in and sign up.
 */
public interface AuthenticationService {

    // Signs a user up
    SignUpResponse signUp(SignUpRequest request);

    // Signs a user in
    JwtAuthenticationResponse signIn(SignInRequest request);

    // Generates a refresh token
    JwtAuthenticationResponse generateRefreshToken(RefreshTokenRequest refreshTokenRequest);
}
