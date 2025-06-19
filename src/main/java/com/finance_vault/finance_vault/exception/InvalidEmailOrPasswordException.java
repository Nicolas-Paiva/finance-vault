package com.finance_vault.finance_vault.exception;

import lombok.Data;

@Data
public class InvalidEmailOrPasswordException extends RuntimeException{

    public InvalidEmailOrPasswordException() {
        super("Invalid email or password");
    }

}
