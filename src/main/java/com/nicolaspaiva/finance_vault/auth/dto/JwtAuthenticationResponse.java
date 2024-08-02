package com.nicolaspaiva.finance_vault.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the JWT response sent
 * to the user after a successful login
 * is performed
 *
 * Contains a token and a refresh token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthenticationResponse {

    private String token;

    private String refreshToken;
}
