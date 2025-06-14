package com.finance_vault.finance_vault.dto.auth;

import com.finance_vault.finance_vault.model.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the DTO the client must
 * provide in order to perform user
 * registration
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotBlank
    @Size(min = 3, message = "Name must have at least three characters")
    private String name;

    @NotBlank
    @Size(min = 3, message = "Name must have at least three characters")
    private String lastName;


    public static User toUser(UserRegistrationRequest userRegistrationRequest) {
        return User.builder()
                .email(userRegistrationRequest.getEmail())
                .password(userRegistrationRequest.getPassword())
                .name(userRegistrationRequest.getName())
                .lastName(userRegistrationRequest.getLastName())
                .build();
    }


    public static UserRegistrationRequest toDTO(User user) {
        return UserRegistrationRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
