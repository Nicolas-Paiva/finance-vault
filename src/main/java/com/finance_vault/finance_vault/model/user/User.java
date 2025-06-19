package com.finance_vault.finance_vault.model.user;

import com.finance_vault.finance_vault.model.transaction.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    // Allows PostgreSQL to
    // generate ID's for new users
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    private float balance;

    @OneToMany(mappedBy = "sender")
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receivedTransactions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist() {
        this.balance = 1000;
        this.createdAt = LocalDateTime.now();
    }

}
