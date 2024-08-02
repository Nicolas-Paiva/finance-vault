package com.nicolaspaiva.finance_vault.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the possible response objects
 * the client might receive depending on the
 * SignUpRequest sent to the server.
 *
 * The response object consists of two fields,
 * the first being a boolean that indicates
 * whether the user was created or not, and the
 * second is a message indicating the error or success
 */
@Data
@NoArgsConstructor
public class SignUpResponse {

    boolean created;

    String message;

    public SignUpResponse(boolean created, String message) {
        this.created = created;
        this.message = message;
    }

    public SignUpResponse success(){
        created = true;
        message = "User created";
        return this;
    }

    public SignUpResponse failed(){
        created = false;
        message = "Failed to create user";
        return this;
    }
}
