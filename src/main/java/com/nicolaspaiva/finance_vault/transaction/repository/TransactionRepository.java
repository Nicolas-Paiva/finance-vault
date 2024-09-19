package com.nicolaspaiva.finance_vault.transaction.repository;

import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
}
