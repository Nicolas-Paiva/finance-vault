package com.finance_vault.finance_vault.controller.transaction;

import com.finance_vault.finance_vault.dto.PaginatedResponse;
import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.dto.transaction.TransactionResponse;
import com.finance_vault.finance_vault.dto.transaction.TransactionView;
import com.finance_vault.finance_vault.exception.UserNotFoundException;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.queryFilter.TransactionQueryFilter;
import com.finance_vault.finance_vault.service.transaction.TransactionService;
import com.finance_vault.finance_vault.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final UserService userService;

    private final TransactionService transactionService;

    /**
     * Creates a transaction between two users.
     */
    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest, Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);

        return ResponseEntity.ok(transactionService.createTransaction(transactionRequest, user));
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions(
            @ModelAttribute TransactionQueryFilter filter,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            Authentication authentication
            ) {

        User user = userService.getUserFromEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);

        // Sets the user in the filter
        filter.setUser(user);

//        PaginatedResponse<TransactionView> transactions = transactionService.getAllTransactions(page, size, user);
        PaginatedResponse<TransactionView> transactions = transactionService
                .getFilteredTransactions(page, size, filter);

        return ResponseEntity.ok(transactions);
    }

}
