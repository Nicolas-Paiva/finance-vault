package com.nicolaspaiva.finance_vault.transaction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the request to
 * perform a transaction.
 *
 * In order to be valid, both the
 * email and the amount must be validated
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionRequestDto {

    private String destinationEmail;

    private double amount;

}
