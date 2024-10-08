package com.nicolaspaiva.finance_vault.auth.confirmationtoken.service;

import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationTokenEntity;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto.ConfirmationTokenResponse;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.repository.ConfirmationTokenRepository;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import com.nicolaspaiva.finance_vault.user.service.UserAccountService;
import com.nicolaspaiva.finance_vault.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final UserAccountService userService;

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


    /**
     * Builds and saves the new user's
     * confirmation token
     */
    public void createConfirmationToken(UserEntity user, String token){

        ConfirmationTokenEntity confirmationToken =
                ConfirmationTokenEntity.builder()
                        .token(token)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().minusMinutes(15))
                        .user(user)
                        .build();

        confirmationTokenRepository.save(confirmationToken);
    }


    /**
     * Generates the string that composes the
     * confirmation token
     */
    public String generateTokenString(){
        return UUID.randomUUID().toString();
    }


    /**
     * Deletes expired tokens every 24h
     *
     * cron syntax: 0s, 0min, 0h, every day, every month, any day of the week
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredTokens(){
        List<ConfirmationTokenEntity> expiredTokens =
                confirmationTokenRepository.findExpiredTokens(DateUtils.getToday());

        confirmationTokenRepository.deleteAll(expiredTokens);
    }
}
