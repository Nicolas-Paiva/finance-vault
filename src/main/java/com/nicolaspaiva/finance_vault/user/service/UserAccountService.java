package com.nicolaspaiva.finance_vault.user.service;

import com.nicolaspaiva.finance_vault.user.dto.UserAccountDto;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;

import java.security.Principal;
import java.util.Optional;

public interface UserAccountService {


    // Retrieves the user account information based on the logged user
    UserAccountDto getUserProfile(String email);

    void activateUser(UserEntity user);

    void saveUser(UserEntity user);

    Optional<UserEntity> findUserByEmail(String email);

    int getUserIdByEmail(String email);
}
