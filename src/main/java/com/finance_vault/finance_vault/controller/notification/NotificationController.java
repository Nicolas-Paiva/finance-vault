package com.finance_vault.finance_vault.controller.notification;

import com.finance_vault.finance_vault.dto.notification.TransactionNotificationDTO;
import com.finance_vault.finance_vault.exception.UserNotFoundException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.notification.NotificationService;
import com.finance_vault.finance_vault.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final UserService userService;

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<List<TransactionNotificationDTO>> getAllNotifications(Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(notificationService.getAllNotifications(user));
    }

}
