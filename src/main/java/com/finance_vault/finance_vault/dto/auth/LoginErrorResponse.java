package com.finance_vault.finance_vault.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginErrorResponse extends LoginResponse {

    private String message;

    public LoginErrorResponse() {
        setSuccess(false);
        message = "Invalid email or password";
    }

}
