package com.nicolaspaiva.finance_vault.transaction.service;

import com.nicolaspaiva.finance_vault.transaction.dto.request.TransactionRequestDto;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionResponseDto;

import java.security.Principal;

public interface TransactionService {

    TransactionResponseDto processTransaction(String email, TransactionRequestDto paymentDto);

}
