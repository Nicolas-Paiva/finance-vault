package com.finance_vault.finance_vault.dto.auth;

import com.finance_vault.finance_vault.model.currency.Currency;
import com.finance_vault.finance_vault.model.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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

    @NotBlank(message = "Name must be provided")
    private String name;

    @NotBlank(message = "Last name must be provided")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;


    public static User toUser(UserRegistrationRequest userRegistrationRequest) {
        Currency currency = null;

        for (Currency curr : Currency.values()) {
            if (curr.name().matches(userRegistrationRequest.getCurrency())) {
                currency = curr;
            }
        }

        return User.builder()
                .email(userRegistrationRequest.getEmail())
                .password(userRegistrationRequest.getPassword())
                .name(userRegistrationRequest.getName())
                .lastName(userRegistrationRequest.getLastName())
                .receivedTransactions(new ArrayList<>())
                .sentTransactions(new ArrayList<>())
                .balance(10000000)
                .currency(currency)
                .build();
    }


    public static UserRegistrationRequest toDTO(User user) {
        return UserRegistrationRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
