package com.finance_vault.finance_vault.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNameChangeRequest {

    private String newName;

    private String newLastName;

}
