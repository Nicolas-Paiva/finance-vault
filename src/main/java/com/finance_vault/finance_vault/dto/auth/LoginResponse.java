package com.finance_vault.finance_vault.dto.auth;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponse {

    private boolean success;

    private LocalDateTime timeStamp;

    public LoginResponse() {
        timeStamp = LocalDateTime.now();
    }

}
