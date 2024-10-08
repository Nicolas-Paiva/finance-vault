package com.nicolaspaiva.finance_vault.auth.confirmationtoken;

import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Confirmation token that is stored in
 * the database for activating a user account
 */
@Entity(name = "confirmation_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity user;

}
