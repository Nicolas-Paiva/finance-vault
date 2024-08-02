package com.nicolaspaiva.finance_vault.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for a
 * refresh token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    private String token;

}
