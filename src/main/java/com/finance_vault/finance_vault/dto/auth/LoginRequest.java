package com.finance_vault.finance_vault.dto.auth;

import com.finance_vault.finance_vault.model.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "An email must be provided")
    private String email;

    @NotBlank(message = "A password must be provided")
    private String password;


    /**
     * Converts a LoginRequest to a user
     */
    public static User toUser(LoginRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

}
