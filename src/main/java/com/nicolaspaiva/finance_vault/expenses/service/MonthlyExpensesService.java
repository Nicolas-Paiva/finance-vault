package com.nicolaspaiva.finance_vault.expenses.service;

import com.nicolaspaiva.finance_vault.expenses.dto.MonthlyResumeDto;
import com.nicolaspaiva.finance_vault.user.dto.UserAccountDto;

/**
 * Service related to the functionality
 * of monthly expenses
 */
public interface MonthlyExpensesService {

    MonthlyResumeDto getMonthlyExpensesAndBalance(String email);

}
