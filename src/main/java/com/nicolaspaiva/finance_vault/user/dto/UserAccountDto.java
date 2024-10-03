package com.nicolaspaiva.finance_vault.user.dto;

import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the user's home page,
 * containing information about the
 * account such as balance and past transactions
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountDto {

    private String firstName;

    private String lastName;

    private float balance;

    List<TransactionDetailsDto> deposits;

    List<TransactionDetailsDto> withdrawals;

}
