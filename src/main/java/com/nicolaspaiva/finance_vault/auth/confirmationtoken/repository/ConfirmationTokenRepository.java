package com.nicolaspaiva.finance_vault.auth.confirmationtoken.repository;

import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Integer> {

    Optional<ConfirmationTokenEntity> findByToken(String token);

    @Query("SELECT t FROM confirmation_tokens t WHERE t.expiresAt < :today")
    List<ConfirmationTokenEntity> findExpiredTokens(@Param("today") LocalDateTime today);

}
