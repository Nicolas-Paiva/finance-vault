package com.finance_vault.finance_vault.service.summary;

import com.finance_vault.finance_vault.dto.summary.MonthlyTransactionsDTO;
import com.finance_vault.finance_vault.dto.summary.SummaryDTO;
import com.finance_vault.finance_vault.dto.summary.WeeklyTotalsDto;
import com.finance_vault.finance_vault.model.user.User;

public interface SummaryService {

    SummaryDTO getSummaryData(User user);

    MonthlyTransactionsDTO getMonthlyTransactions(User user);

    WeeklyTotalsDto getMonthWeeklyTotals(User user);
}
