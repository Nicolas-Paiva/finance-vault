package com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationTokenRequest {

    String confirmationToken;

}
