package com.finance_vault.finance_vault.dto.auth;

import com.finance_vault.finance_vault.dto.user.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationResponse {

    private boolean created;

    private String message;


    /**
     * Returns the success message when the user is
     * created successfully
     */
    public static RegistrationResponse success(UserRegistrationRequest userRegistrationRequest) {
        return RegistrationResponse.builder()
                .created(true)
                .message("User " + userRegistrationRequest.getEmail() + " created successfully!")
                .build();
    }

    public static RegistrationResponse failed(String message) {
        return RegistrationResponse.builder()
                .created(false)
                .message(message)
                .build();
    }

}
