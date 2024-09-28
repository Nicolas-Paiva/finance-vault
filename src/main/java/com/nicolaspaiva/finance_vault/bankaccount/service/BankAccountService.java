package com.nicolaspaiva.finance_vault.bankaccount.service;


import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;

public interface BankAccountService {

    BankAccountEntity createBankAccount(UserEntity user);

}
