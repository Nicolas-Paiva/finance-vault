package com.nicolaspaiva.finance_vault.transaction.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionDetailsDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity that stores all the information
 * about performed transactions
 */
@Entity
@Table(name = "transactions")
@Data
@ToString(exclude = {"senderAccountId", "receiverAccountId"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private BankAccountEntity senderAccountId;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private BankAccountEntity receiverAccountId;

    private String senderEmail;

    private String receiverEmail;

    private float amount;

    private LocalDateTime createdAt;


    public static TransactionDetailsDto entityToTransactionDetails(TransactionEntity transactionEntity){
        return TransactionDetailsDto.builder()
                .amount(transactionEntity.amount)
                .createdAt(transactionEntity.createdAt)
                .receiverEmail(transactionEntity.receiverEmail)
                .senderEmail(transactionEntity.senderEmail)
                .build();
    }

}
