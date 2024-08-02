package com.nicolaspaiva.finance_vault.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the object that
 * should be sent in order to
 * sign in
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    private String email;

    private String password;

}
