package com.finance_vault.finance_vault.utils;

public class Utils {

    /**
     * Validates a password.
     * A valid password contains at least
     * 8 characters, one uppercase letter
     * and at least one symbol
     */
    public static boolean isPasswordValid(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$";
        return password.matches(regex);
    }

    public static boolean isEmailFormatValid(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return email.matches(emailRegex);
    }

}
