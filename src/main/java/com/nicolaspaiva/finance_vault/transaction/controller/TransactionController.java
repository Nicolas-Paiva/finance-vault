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
     * Endpoint designed to send money to other bank accounts.
     *
     * In order to send an amount, the PaymentDto must be created,
     * which consists of the email of the receiver account,
     * as well as the amount to be sent.
     *
     * @param transactionDto The transaction object consists of
     * the destination account's email as well as the
     * amount to be sent
     *
     *
     * @return a TransactionResponseDto,
     * which can be successful or not
     */
    @RequestMapping("/transfer")
    public ResponseEntity<?> processTransfer(Principal principal, @RequestBody TransactionRequestDto transactionDto){

        TransactionResponseDto response = transactionService.processTransaction(principal, transactionDto);

        if(response.isSuccess()){
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
    }

}
