package com.finance_vault.finance_vault.repository;

import com.finance_vault.finance_vault.model.transaction.Transaction;

import com.finance_vault.finance_vault.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {

    @Query("SELECT t FROM Transaction t WHERE t.sender = :user OR t.receiver = :user")
    Page<Transaction> findAllByUser(@Param("user") User user, Pageable pageable);


    @Query("SELECT t FROM Transaction t WHERE t.receiver = :user AND t.createdAt BETWEEN :startOfMonth AND :currentDay")
    List<Transaction> findAllMonthlyDeposits(@Param("user") User user,
                                             @Param("startOfMonth") LocalDateTime startOfMonth,
                                             @Param("currentDay") LocalDateTime currentDay);

    @Query("SELECT t FROM Transaction t WHERE t.sender = :user AND t.createdAt BETWEEN :startOfMonth AND :currentDay")
    List<Transaction> findAllMonthlyWithdrawals(@Param("user") User user,
                                             @Param("startOfMonth") LocalDateTime startOfMonth,
                                             @Param("currentDay") LocalDateTime currentDay);

}
