package com.nicolaspaiva.finance_vault.auth.token.service;


import com.nicolaspaiva.finance_vault.auth.token.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);

    Optional<ConfirmationToken> getConfirmationToken(String token);

    void setConfirmedAt(ConfirmationToken token);

}
