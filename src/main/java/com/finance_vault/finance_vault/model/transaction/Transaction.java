package com.finance_vault.finance_vault.model.transaction;

import com.finance_vault.finance_vault.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
