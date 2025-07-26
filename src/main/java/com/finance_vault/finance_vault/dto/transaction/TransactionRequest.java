package com.finance_vault.finance_vault.dto.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionRequest {

    private String receiverEmail;

    private float amount;

}
