package com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto that represents the possible
 * objects sent to the client regarding
 * their account activation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationTokenResponse {

    private boolean success;

    private String message;

    public static ConfirmationTokenResponse tokenIsValid(){
        return new ConfirmationTokenResponse(true, "Your account has been verified");
    }

    public static ConfirmationTokenResponse tokenExpired(){
        return new ConfirmationTokenResponse(false, "Token has already expired");
    }

    public static ConfirmationTokenResponse invalidToken(){
        return new ConfirmationTokenResponse(false, "Token does not exist");
    }

    public static ConfirmationTokenResponse tokenAlreadyVerified(){
        return new ConfirmationTokenResponse(false, "Token has already been verified");
    }



}
