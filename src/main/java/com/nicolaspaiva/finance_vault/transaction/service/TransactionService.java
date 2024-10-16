package com.nicolaspaiva.finance_vault.transaction.service;

import com.nicolaspaiva.finance_vault.transaction.dto.request.TransactionRequestDto;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionResponseDto;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionType;

import java.util.List;

public interface TransactionService {

    TransactionResponseDto processTransaction(String email, TransactionRequestDto paymentDto);

    List<Double> getUserMonthlyTransactionValuesByEmail(String email, TransactionType transactionType);

    List<TransactionEntity> getMonthlyTransactions();

    int countMonthlyTransactions();

    double getMonthlyTransactionVolume();

    double getMonthlyTransactionFees();
}
