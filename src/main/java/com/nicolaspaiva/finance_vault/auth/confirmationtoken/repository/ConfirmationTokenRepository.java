package com.nicolaspaiva.finance_vault.auth.confirmationtoken.repository;

import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Integer> {

    Optional<ConfirmationTokenEntity> findByToken(String token);

}
