package com.finance_vault.finance_vault.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {

    private boolean created;

    private LocalDateTime timeStamp;

    private String message;

    private String jwtToken;


    /**
     * Returns the success message when the user is
     * created successfully
     */
    public static RegistrationResponse success(UserRegistrationRequest userRegistrationRequest, String jwt) {
        return RegistrationResponse.builder()
                .created(true)
                .timeStamp(LocalDateTime.now())
                .message("User " + userRegistrationRequest.getEmail() + " created successfully!")
                .jwtToken(jwt)
                .build();
    }

    public static RegistrationResponse failed(String message) {
        return RegistrationResponse.builder()
                .timeStamp(LocalDateTime.now())
                .created(false)
                .message(message)
                .build();
    }

}
