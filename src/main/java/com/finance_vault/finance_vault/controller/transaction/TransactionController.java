package com.finance_vault.finance_vault.controller.transaction;

import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.exception.UserNotFoundException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.transaction.TransactionService;
import com.finance_vault.finance_vault.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    // TODO: Create transaction service
    private final UserService userService;

    private final TransactionService transactionService;

    /**
     * Creates a transaction between two users.
     */
    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest, Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);

        return ResponseEntity.ok(transactionService.createTransaction(transactionRequest, user));
    }

}
