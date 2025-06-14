package com.finance_vault.finance_vault.service.user;

import com.finance_vault.finance_vault.model.user.User;
import org.springframework.security.core.Authentication;

public interface UserService {

    public User getUserFromAuthentication(Authentication authentication);

}
