package com.nicolaspaiva.finance_vault.auth.confirmationtoken.service;

import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationToken;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private final ConfirmationTokenRepository confirmationTokenRepository;

    // TODO: Create token deletion method to clear up used and expired tokens after 24h

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getConfirmationToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(ConfirmationToken confirmationToken){
        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }
}
