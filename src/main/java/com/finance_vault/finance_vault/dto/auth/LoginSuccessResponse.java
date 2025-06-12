package com.finance_vault.finance_vault.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginSuccessResponse extends LoginResponse {

    private String jwtToken;

    public LoginSuccessResponse(String jwtToken) {
        setSuccess(true);
        this.jwtToken = jwtToken;
    }

}
