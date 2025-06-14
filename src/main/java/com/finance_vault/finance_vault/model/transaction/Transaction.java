package com.finance_vault.finance_vault.model.transaction;

import com.finance_vault.finance_vault.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "transactions")
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

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User receiver;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User sender;


    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
