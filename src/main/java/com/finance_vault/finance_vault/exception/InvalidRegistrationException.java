package com.finance_vault.finance_vault.exception;

import lombok.Data;

@Data
public class InvalidRegistrationException extends RuntimeException {

    private String message;

    public static InvalidRegistrationException userEmailAlreadyExists() {
        InvalidRegistrationException e = new InvalidRegistrationException();
        e.setMessage("Email already exists");
        return e;
    }

    public static InvalidRegistrationException invalidPassword() {
        InvalidRegistrationException e = new InvalidRegistrationException();
        e.setMessage("Password must contain at least 8 characters, 1 uppercase letter and one symbol (#,._@) ");
        return e;
    }

}
