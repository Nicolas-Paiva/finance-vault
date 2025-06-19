package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.PaginatedResponse;
import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.dto.transaction.TransactionResponse;
import com.finance_vault.finance_vault.dto.transaction.TransactionView;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest transactionRequest, User user);


    PaginatedResponse<TransactionView> getAllTransactions(int page, int size, User user);


    float getMonthlyDepositsTotal(User user);

    float getMonthlyWithdrawalsTotal(User user);
}
