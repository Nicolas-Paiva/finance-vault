package com.nicolaspaiva.finance_vault.transaction.controller;

import com.nicolaspaiva.finance_vault.transaction.dto.request.TransactionRequestDto;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionResponseDto;
import com.nicolaspaiva.finance_vault.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/transactions")
public class TransactionController {

    private final TransactionService transactionService;


    /**
     * Endpoint for transferring money between bank accounts.
     *
     * A valid TransactionRequestDto is required to initiate a transaction.
     *
     * If the transaction is successful, it returns a 201 (Created) status along with
     * the TransactionResponseDto indicating success.
     *
     * If the transaction fails due to invalid data or other reasons, a 400 (Bad Request) status
     * is returned along with the TransactionResponseDto describing the error.
     */
    @RequestMapping("/transfer")
    public ResponseEntity<?> processTransfer(Principal principal, @RequestBody TransactionRequestDto transactionDto){

        TransactionResponseDto response = transactionService.processTransaction(principal.getName(), transactionDto);

        if(response.isSuccess()){
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
    }

}
