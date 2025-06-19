package com.finance_vault.finance_vault.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailChangeRequest {

    @NotBlank(message = "Please provide a valid email")
    private String newEmail;

    @NotBlank(message = "Please provide a valid confirmation email")
    private String newEmailConfirmation;

}
