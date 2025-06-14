package com.finance_vault.finance_vault.dto.transaction;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String receiverEmail;

    @Positive(message = "Transaction value must be positive")
    private float amount;

}
