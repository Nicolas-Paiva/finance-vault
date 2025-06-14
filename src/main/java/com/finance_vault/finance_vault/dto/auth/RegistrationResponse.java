package com.finance_vault.finance_vault.dto.auth;

import com.finance_vault.finance_vault.dto.user.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class RegistrationResponse {

    private boolean created;

    private LocalDateTime timeStamp;

    private String message;


    /**
     * Returns the success message when the user is
     * created successfully
     */
    public static RegistrationResponse success(UserRegistrationRequest userRegistrationRequest) {
        return RegistrationResponse.builder()
                .created(true)
                .timeStamp(LocalDateTime.now())
                .message("User " + userRegistrationRequest.getEmail() + " created successfully!")
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
