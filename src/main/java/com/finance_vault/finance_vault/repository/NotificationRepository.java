package com.finance_vault.finance_vault.repository;

import com.finance_vault.finance_vault.model.notification.TransactionNotification;
import com.finance_vault.finance_vault.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<TransactionNotification, Long> {
    List<TransactionNotification> findByUser(User user);

}
