package com.nicolaspaiva.finance_vault.bankaccount.repository;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {

    Optional<BankAccountEntity> findBankAccountEntityByOwnerEmail(String email);

}
