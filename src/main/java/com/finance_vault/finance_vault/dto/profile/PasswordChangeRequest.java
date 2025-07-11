package com.finance_vault.finance_vault.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

    @NotBlank(message = "Please provide a password")
    private String oldPassword;

    @NotBlank(message = "Please provide a password")
    private String newPassword;

    @NotBlank(message = "Please provide a confirmation password")
    private String newPasswordConfirmation;

}
