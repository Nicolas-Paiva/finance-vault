package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.PaginatedResponse;
import com.finance_vault.finance_vault.dto.transaction.*;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.queryFilter.TransactionQueryFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest transactionRequest, User user);


    PaginatedResponse<TransactionView> getFilteredTransactions(int page, int size, TransactionQueryFilter filter,
                                                               String sortBy, String order);


    float getMonthlyDepositsTotal(User user);


    float getMonthlyWithdrawalsTotal(User user);

    FundsResponse getFunds(FundsRequest request, User user);
}
