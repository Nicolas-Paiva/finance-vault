package com.finance_vault.finance_vault.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private boolean success;

    private String message;

    private LocalDateTime timestamp;

    public static TransactionResponse success() {
        return TransactionResponse.builder()
                .success(true)
                .message("Transaction successful!")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static  TransactionResponse failed(String message) {
        return TransactionResponse.builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
