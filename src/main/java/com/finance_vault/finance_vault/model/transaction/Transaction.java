package com.finance_vault.finance_vault.model.transaction;

import com.finance_vault.finance_vault.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_seq",
            sequenceName = "transaction_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_seq"
    )
    private Long id;

    @ManyToOne
    private User user;
}
