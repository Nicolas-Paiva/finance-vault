package com.nicolaspaiva.finance_vault.auth.confirmationtoken.service;

import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationTokenEntity;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto.ConfirmationTokenResponse;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.repository.ConfirmationTokenRepository;
import com.nicolaspaiva.finance_vault.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final UserAccountService userService;

    // TODO: Create token deletion method to clear up used and expired tokens after 24h

    @Override
    public void saveConfirmationToken(ConfirmationTokenEntity token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationTokenEntity> getConfirmationToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(ConfirmationTokenEntity confirmationToken){
        confirmationToken.setConfirmedAt(LocalDateTime.now());
    }

    @Override
    @Transactional
    public ConfirmationTokenResponse verifyToken(String token){

        ConfirmationTokenEntity confirmationToken;

        Optional<ConfirmationTokenEntity> confirmationTokenOpt = getConfirmationToken(token);

        if(confirmationTokenOpt.isEmpty()){
            return ConfirmationTokenResponse.invalidToken();
        } else {
            confirmationToken = confirmationTokenOpt.get();
        }

        if(confirmationToken.getConfirmedAt() != null){
            return ConfirmationTokenResponse.tokenAlreadyVerified();
        }

        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            return ConfirmationTokenResponse.tokenExpired();
        }

        setConfirmedAt(confirmationToken);

        userService.activateUser(confirmationToken.getUser());

        userService.saveUser(confirmationToken.getUser());

        return ConfirmationTokenResponse.tokenIsValid();
    }
}
