package com.finance_vault.finance_vault.model.notification;

import com.finance_vault.finance_vault.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionNotification {

    @Id
    // Allows PostgreSQL to
    // generate ID's for new users
    @SequenceGenerator(
            name = "notification_seq",
            sequenceName = "notification_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_seq"
    )
    private Long id;

    @NotBlank
    private String message;

    @Positive(message = "amount must be positive")
    private float amount;

    private LocalDateTime createdAt;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
