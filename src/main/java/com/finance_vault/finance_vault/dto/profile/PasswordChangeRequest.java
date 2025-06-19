package com.finance_vault.finance_vault.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

    private String newPassword;

    private String newPasswordConfirmation;

}
