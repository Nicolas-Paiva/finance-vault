package com.nicolaspaiva.finance_vault.bankaccount.entity;

import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the user's bank account
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ownerEmail;

    float balance;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private UserEntity owner;

}
