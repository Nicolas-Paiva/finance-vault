package com.finance_vault.finance_vault.service.notification;

import com.finance_vault.finance_vault.dto.notification.TransactionNotificationDTO;
import com.finance_vault.finance_vault.model.notification.TransactionNotification;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;


    @Override
    public List<TransactionNotificationDTO> getAllNotifications(User user) {
        return notificationRepository.findByUser(user).stream().map(TransactionNotificationDTO::toDTO).toList();
    }


    @Override
    public void addTransactionNotification(Transaction transaction) {
        TransactionNotification notification = TransactionNotification.builder()
                .message("Transaction received from: " + transaction.getSender().getName())
                .amount(transaction.getAmount())
                .user(transaction.getReceiver())
                .build();

        notificationRepository.save(notification);
    }

}
