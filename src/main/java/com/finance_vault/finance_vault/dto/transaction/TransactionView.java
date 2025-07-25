package com.finance_vault.finance_vault.dto.transaction;

import com.finance_vault.finance_vault.model.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionView {

    private Long id;

    private float amount;

    private LocalDateTime createdAt;

    private String senderName;

    private String receiverName;

    private String receiverEmail;

    private String senderEmail;


    // Returns the DTO as a withdrawal
    public static TransactionView toWithdrawal(Transaction transaction) {
        return TransactionView.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .receiverEmail(transaction.getReceiver().getEmail())
                .senderEmail("")
                .senderName("")
                .createdAt(transaction.getCreatedAt())
                .receiverName(transaction.getReceiver().getName() + " " + transaction.getReceiver().getLastName())
                .build();
    }


    // Returns the DTO as a deposit
    public static TransactionView toDeposit(Transaction transaction) {
        return TransactionView.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .receiverEmail("")
                .senderEmail(transaction.getSender().getEmail())
                .createdAt(transaction.getCreatedAt())
                .receiverName("")
                .senderName(transaction.getSender().getName() + " " + transaction.getSender().getLastName())
                .build();
    }

}
