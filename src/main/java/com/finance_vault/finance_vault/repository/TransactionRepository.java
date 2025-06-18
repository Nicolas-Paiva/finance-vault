package com.finance_vault.finance_vault.repository;

import com.finance_vault.finance_vault.model.transaction.Transaction;

import com.finance_vault.finance_vault.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM transactions t WHERE t.sender = :user OR t.receiver = :user")
    Page<Transaction> findAllByUser(@Param("user") User user, Pageable pageable);




}
