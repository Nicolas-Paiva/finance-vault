package com.finance_vault.finance_vault.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
