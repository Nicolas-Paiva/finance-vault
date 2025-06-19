package com.finance_vault.finance_vault.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDataChangeResponse {

    private boolean success;

    private String message;

    public static ProfileDataChangeResponse successEmailChanged() {
        return new ProfileDataChangeResponse(true,
                "Email changed successfully");
    }


    public static ProfileDataChangeResponse successPasswordChanged() {
        return new ProfileDataChangeResponse(true,
                "Password changed successfully");
    }
}
