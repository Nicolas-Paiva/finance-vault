package com.finance_vault.finance_vault.controller.transaction;

import com.finance_vault.finance_vault.dto.PaginatedResponse;
import com.finance_vault.finance_vault.dto.transaction.FundsRequest;
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
        User user = userService.getUserFromEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        return ResponseEntity.ok(transactionService.createTransaction(transactionRequest, user));
    }


    /**
     * Returns a Pageable response with TransactionViews of the user.
     *
     * The transactions can be filtered and sorted by passing
     * query parameters in the URL.
     *
     * The accepted filters are:
     * - type: "deposit" OR "withdrawal"
     * - minValue
     * - maxValue
     *
     * SortBy properties are:
     * createdAt OR amount
     */
    @GetMapping("/transactions")
    public ResponseEntity<PaginatedResponse<TransactionView>> getAllTransactions(
            @ModelAttribute TransactionQueryFilter filter,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Authentication authentication
            ) {

        User user = userService.getUserFromEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        // Sets the user in the filter
        filter.setUser(user);

        PaginatedResponse<TransactionView> transactions = transactionService
                .getFilteredTransactions(page, size, filter, sortBy, order);

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transactions/add")
    public ResponseEntity<?> addFunds(Authentication authentication, @RequestBody FundsRequest request) {
        User user = userService.getUserFromEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        return ResponseEntity.ok(transactionService.getFunds(request, user));
    }

}
