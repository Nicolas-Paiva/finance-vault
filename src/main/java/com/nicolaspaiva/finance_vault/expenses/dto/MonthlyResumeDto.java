package com.nicolaspaiva.finance_vault.expenses.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the monthly resume
 * sent to the client, containing
 * the total expenses, the deposits
 * and the monthly balance
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyResumeDto {

    private double totalExpenses;

    private double totalDeposits;

    private double monthlyBalance;

}
