package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.dto.transaction.TransactionResponse;
import com.finance_vault.finance_vault.model.user.User;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionRequest transactionRequest, User user);

}
