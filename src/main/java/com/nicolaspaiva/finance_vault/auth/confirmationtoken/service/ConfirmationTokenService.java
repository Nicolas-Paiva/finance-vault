package com.nicolaspaiva.finance_vault.auth.confirmationtoken.service;


import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationTokenEntity;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto.ConfirmationTokenResponse;

import java.util.Optional;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationTokenEntity token);

    Optional<ConfirmationTokenEntity> getConfirmationToken(String token);

    void setConfirmedAt(ConfirmationTokenEntity token);

    ConfirmationTokenResponse verifyToken(String token);

}
