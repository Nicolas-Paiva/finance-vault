package com.nicolaspaiva.finance_vault.auth.confirmationtoken.service;


import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);

    Optional<ConfirmationToken> getConfirmationToken(String token);

    void setConfirmedAt(ConfirmationToken token);

}
