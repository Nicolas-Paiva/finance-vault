package com.finance_vault.finance_vault.service.user;

import com.finance_vault.finance_vault.model.user.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserFromEmail(String email);

    void changeUserEmail(User user, String newEmail);

    void changeUserPassword(User user, String newPassword);
}
