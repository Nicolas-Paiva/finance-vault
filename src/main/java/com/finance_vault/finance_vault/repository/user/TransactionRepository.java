package com.finance_vault.finance_vault.repository.user;

import com.finance_vault.finance_vault.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
