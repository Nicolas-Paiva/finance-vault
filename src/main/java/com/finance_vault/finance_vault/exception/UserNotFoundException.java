package com.finance_vault.finance_vault.exception;

import lombok.Data;

@Data
public class UserNotFoundException extends RuntimeException {


    public UserNotFoundException() {
        super("User not found");
    }

}
