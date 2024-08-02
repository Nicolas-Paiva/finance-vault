package com.nicolaspaiva.finance_vault.bankaccount.service;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.BankAccountRepository;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{

    private final BankAccountRepository bankAccountRepository;

    public BankAccountEntity createBankAccount(UserEntity user){

        return BankAccountEntity.builder()
                .owner(user)
                .ownerEmail(user.getEmail())
                .balance(0)
                .build();

    }

}
