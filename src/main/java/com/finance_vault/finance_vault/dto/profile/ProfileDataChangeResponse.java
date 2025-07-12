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

    private String jwt;

    public static ProfileDataChangeResponse successEmailChanged(String jwt) {
        return new ProfileDataChangeResponse(true,
                "Email changed successfully", jwt);
    }


    public static ProfileDataChangeResponse successPasswordChanged(String jwt) {
        return new ProfileDataChangeResponse(true,
                "Password changed successfully", jwt);
    }

    public static ProfileDataChangeResponse successNameChanged(String jwt) {
        return new ProfileDataChangeResponse(true,
                "Name changed successfully", jwt);
    }
}
