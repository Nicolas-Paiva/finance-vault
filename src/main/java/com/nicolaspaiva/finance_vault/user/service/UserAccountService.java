package com.nicolaspaiva.finance_vault.user.service;

import com.nicolaspaiva.finance_vault.user.dto.UserAccountDto;

import java.security.Principal;

public interface UserAccountService {


    // Retrieves the user account information based on the logged user
    UserAccountDto getUserProfile(Principal principal);
}
