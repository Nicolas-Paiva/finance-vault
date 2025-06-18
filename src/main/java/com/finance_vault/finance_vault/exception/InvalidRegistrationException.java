package com.finance_vault.finance_vault.exception;

import lombok.Data;

@Data
public class InvalidRegistrationException extends RuntimeException {

    public InvalidRegistrationException(String message) {
        super(message);
    }

    public static InvalidRegistrationException userEmailAlreadyExists() {
        return new InvalidRegistrationException("Email already exists");
    }

    public static InvalidRegistrationException invalidPassword() {
        return new InvalidRegistrationException("Password must contain at least 8 characters," +
                " 1 uppercase letter and one symbol (#,._@)");
    }

    public static InvalidRegistrationException invalidEmail() {
        return new InvalidRegistrationException("Invalid email format");
    }

    public static InvalidRegistrationException invalidCurrency() {
        return new InvalidRegistrationException("Please provide a supported currency");
    }

}
