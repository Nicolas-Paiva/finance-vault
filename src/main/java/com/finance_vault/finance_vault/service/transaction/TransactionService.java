package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.PaginatedResponse;
import com.finance_vault.finance_vault.dto.summary.MonthlyTransactionsDTO;
import com.finance_vault.finance_vault.dto.summary.WeeklyTotalsDto;
import com.finance_vault.finance_vault.dto.transaction.*;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.queryFilter.TransactionQueryFilter;


public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest transactionRequest, User user);


    PaginatedResponse<TransactionView> getFilteredTransactions(int page, int size, TransactionQueryFilter filter,
                                                               String sortBy, String order);


    float getMonthlyDepositsTotal(User user);


    float getMonthlyWithdrawalsTotal(User user);


    MonthlyTransactionsDTO getMonthlyTransactions(User user);


    WeeklyTotalsDto getMonthWeeklyWithdrawals(User user);


    FundsResponse getFunds(FundsRequest request, User user);

}
