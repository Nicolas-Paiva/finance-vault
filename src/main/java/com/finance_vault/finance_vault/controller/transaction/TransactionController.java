package com.finance_vault.finance_vault.controller.transaction;

import com.finance_vault.finance_vault.dto.transaction.TransactionDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    // TODO: Create transaction service

    /**
     * Creates a transaction between two users.
     */
    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionDTO);
    }

}
