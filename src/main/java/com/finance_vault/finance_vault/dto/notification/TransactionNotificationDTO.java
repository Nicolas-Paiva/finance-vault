package com.finance_vault.finance_vault.dto.notification;

import com.finance_vault.finance_vault.model.notification.TransactionNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionNotificationDTO {

    private String message;

    private float amount;

    private LocalDateTime createdAt;

    private boolean seen;

    public static TransactionNotificationDTO toDTO(TransactionNotification notification) {
        return new TransactionNotificationDTO(notification.getMessage(),
                notification.getAmount(),
                notification.getCreatedAt(),
                notification.isSeen());
    }

}
