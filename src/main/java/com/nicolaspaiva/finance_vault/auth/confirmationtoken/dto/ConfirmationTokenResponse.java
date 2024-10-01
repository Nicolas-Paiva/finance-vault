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

    private String status;

    private String message;

    public static ConfirmationTokenResponse tokenIsValid(){
        return new ConfirmationTokenResponse("Success", "Your account has been verified");
    }

    public static ConfirmationTokenResponse tokenExpired(){
        return new ConfirmationTokenResponse("Failed", "Token has already expired");
    }

    public static ConfirmationTokenResponse invalidToken(){
        return new ConfirmationTokenResponse("Failed", "Token does not exist");
    }

    public static ConfirmationTokenResponse tokenAlreadyVerified(){
        return new ConfirmationTokenResponse("Failed", "Token has already been verified");
    }



}
