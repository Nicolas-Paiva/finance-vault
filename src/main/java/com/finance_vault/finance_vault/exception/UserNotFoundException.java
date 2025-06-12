package com.finance_vault.finance_vault.exception;

import lombok.Data;

@Data
public class UserNotFoundException extends RuntimeException {

    private String message;

    public UserNotFoundException() {
        message = "User not found";
    }

}
