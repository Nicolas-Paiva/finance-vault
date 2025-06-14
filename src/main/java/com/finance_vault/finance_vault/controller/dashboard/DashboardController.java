package com.finance_vault.finance_vault.controller.dashboard;

import com.finance_vault.finance_vault.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint designed to provide
 * access to user related data,
 * such as name, balance and transactions.
 */
@RestController
@RequiredArgsConstructor
public class DashboardController {

    // TODO: Get the user by email and retrieve data
    // TODO: Create UserService for user related data
    // TODO: Create TransactionService for transactions
    // TODO: Create Dashboard service to invoke these services

    @GetMapping("/dashboard")
    public ResponseEntity<?> getSummary(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }

}
