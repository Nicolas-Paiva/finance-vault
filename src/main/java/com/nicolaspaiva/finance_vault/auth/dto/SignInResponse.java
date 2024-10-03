package com.nicolaspaiva.finance_vault.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {

    private boolean success;

    private String jwt;

    private String message;

    public static SignInResponse success(String jwt){
        return new SignInResponse(true, jwt, "Sign in successful");
    }

    public static SignInResponse userDoesNotExist(){
        return new SignInResponse(false, null, "User does not exist");
    }

    public static SignInResponse userIsInactive(){
        return new SignInResponse(false, null, "User is not active");
    }

}
