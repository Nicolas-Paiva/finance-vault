package com.finance_vault.finance_vault.exception;

import lombok.Data;

@Data
public class InvalidEmailOrPassword extends RuntimeException{

    private String message;

    public InvalidEmailOrPassword() {
        message = "Invalid email or password";
    }

}
