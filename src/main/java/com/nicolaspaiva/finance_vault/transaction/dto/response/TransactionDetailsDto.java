package com.nicolaspaiva.finance_vault.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the transaction details
 * that the user should be able to see
 * in the UI.
 *
 * Used when the user retrieves
 * previous transactions
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailsDto {

    private String senderEmail;

    private String receiverEmail;

    private double amount;

    private LocalDateTime createdAt;

}
