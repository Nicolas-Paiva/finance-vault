package com.finance_vault.finance_vault.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents the error message the client will receive when
 * the data provided for the login is not valid
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginErrorResponse extends LoginResponse {

    private String message;

    public LoginErrorResponse() {
        super();
        setSuccess(false);
        message = "Invalid email or password";
    }

}
