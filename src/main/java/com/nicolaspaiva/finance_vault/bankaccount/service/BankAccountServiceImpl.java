package com.nicolaspaiva.finance_vault.bankaccount.service;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{


    public BankAccountEntity createBankAccount(UserEntity user){

        return BankAccountEntity.builder()
                .ownerEmail(user.getEmail())
                .balance(10000)
                .build();

    }

}
