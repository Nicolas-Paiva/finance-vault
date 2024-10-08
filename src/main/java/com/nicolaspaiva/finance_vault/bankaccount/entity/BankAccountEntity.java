package com.nicolaspaiva.finance_vault.bankaccount.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Represents the user's bank account
 */
@Entity
@Table(name = "bank_accounts")
@Data
@ToString(exclude = {"deposits", "withdrawals"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ownerEmail;

    double balance;

    @JsonBackReference
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "receiverAccountId")
    List<TransactionEntity> deposits;

    @JsonBackReference
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "senderAccountId")
    List<TransactionEntity> withdrawals;

    public void addDeposit(TransactionEntity transaction){
        deposits.add(transaction);
    }

    public void addWithdrawal(TransactionEntity transaction){
        deposits.add(transaction);
    }

}
