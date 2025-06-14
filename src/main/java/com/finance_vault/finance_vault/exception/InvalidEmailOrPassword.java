package com.finance_vault.finance_vault.exception;

import lombok.Data;

@Data
public class InvalidEmailOrPassword extends RuntimeException{

    public InvalidEmailOrPassword() {
        super("Invalid email or password");
    }

}
