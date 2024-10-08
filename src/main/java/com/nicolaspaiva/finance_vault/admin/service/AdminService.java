package com.nicolaspaiva.finance_vault.admin.service;

import com.nicolaspaiva.finance_vault.admin.dto.AdminDashboard;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<UserEntity> getAllUsers(String sort);

    Optional<UserEntity> getUserByEmail(String email);

    AdminDashboard getDashboard();

}
