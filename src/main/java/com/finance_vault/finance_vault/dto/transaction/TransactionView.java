package com.finance_vault.finance_vault.dto.transaction;

import com.finance_vault.finance_vault.model.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionView {

    private Long id;

    private float amount;

    private LocalDateTime createdAt;

    private String senderEmail;

    private String receiverEmail;

    // Returns the DTO as a withdrawal
    public static TransactionView toWithdrawal(Transaction transaction) {
        return new TransactionView(transaction.getId(), transaction.getAmount(),
                transaction.getCreatedAt(), null, transaction.getReceiver().getEmail());
    }

    // Returns the DTO as a deposit
    public static TransactionView toDeposit(Transaction transaction) {
        return new TransactionView(transaction.getId(), transaction.getAmount(),
                transaction.getCreatedAt(), transaction.getSender().getEmail(), null);
    }
}
