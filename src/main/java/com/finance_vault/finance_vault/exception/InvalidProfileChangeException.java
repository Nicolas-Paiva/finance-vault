package com.finance_vault.finance_vault.exception;

public class InvalidProfileChangeException extends RuntimeException {
    public InvalidProfileChangeException(String message) {
        super(message);
    }


    public static InvalidProfileChangeException invalidOldPassword() {
        return new InvalidProfileChangeException("Please provide your current password");
    }

    public static InvalidProfileChangeException invalidPassword() {
        return new InvalidProfileChangeException("Please provide a valid password. A valid password must contain at " +
                "least 8 characters," +
                " 1 uppercase letter and 1 special character (, . @ $)");
    }


    public static InvalidProfileChangeException passwordMustMatch() {
        return new InvalidProfileChangeException("Both passwords must match");
    }


    public static InvalidProfileChangeException invalidEmail() {
        return new InvalidProfileChangeException("Please provide a valid email format");
    }


    public static InvalidProfileChangeException emailMustMatch() {
        return new InvalidProfileChangeException("Both email must match");
    }


    public static InvalidProfileChangeException emailAlreadyExists() {
        return new InvalidProfileChangeException("The provided email already has a profile linked to it");
    }

}
