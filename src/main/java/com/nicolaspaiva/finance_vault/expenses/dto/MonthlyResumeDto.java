package com.nicolaspaiva.finance_vault.expenses.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyResumeDto {

    private float totalExpenses;

    private float totalDeposits;

    private float monthlyBalance;

}
