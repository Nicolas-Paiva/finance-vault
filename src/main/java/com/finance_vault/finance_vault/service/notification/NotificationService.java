package com.finance_vault.finance_vault.service.notification;

import com.finance_vault.finance_vault.dto.notification.TransactionNotificationDTO;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;

import java.util.List;

public interface NotificationService {

    List<TransactionNotificationDTO> getAllNotifications(User user);

    void addTransactionNotification(Transaction transaction);

}
