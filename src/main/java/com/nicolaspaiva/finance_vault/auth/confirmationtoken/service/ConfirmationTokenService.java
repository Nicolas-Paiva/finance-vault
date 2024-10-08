package com.nicolaspaiva.finance_vault.auth.confirmationtoken.service;


import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationTokenEntity;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto.ConfirmationTokenResponse;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationTokenEntity token);

    Optional<ConfirmationTokenEntity> getConfirmationToken(String token);

    void setConfirmedAt(ConfirmationTokenEntity token);

    ConfirmationTokenResponse verifyToken(String token);

    void createConfirmationToken(UserEntity user, String token);

    String generateTokenString();

}
