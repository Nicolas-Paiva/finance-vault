package com.nicolaspaiva.finance_vault.bankaccount;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {
}
