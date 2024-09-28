package com.nicolaspaiva.finance_vault.transaction.repository;

import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    @Query("SELECT t FROM TransactionEntity t " +
            "WHERE t.senderAccountId.id = :accountId " +
            "AND t.createdAt BETWEEN :startDate AND :endDate")
    List<TransactionEntity> findWithdrawalsByIdAndDateRange(@Param("accountId") Integer accountId,
                                                            @Param("startDate") LocalDateTime startDate,
                                                            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM TransactionEntity t " +
            "WHERE t.receiverAccountId.id = :accountId " +
            "AND t.createdAt BETWEEN :startDate AND :endDate")
    List<TransactionEntity> findDepositsByIdAndDateRange(@Param("accountId") Integer accountId,
                                                            @Param("startDate") LocalDateTime startDate,
                                                            @Param("endDate") LocalDateTime endDate);
}
