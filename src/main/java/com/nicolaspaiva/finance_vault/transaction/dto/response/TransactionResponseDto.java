package com.nicolaspaiva.finance_vault.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the objects sent to the client
 * after performing a transaction.
 *
 * The transaction can either be successful or not,
 * depending on the email and amount provided
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionResponseDto {

    private boolean success;

    private String message;


    public static TransactionResponseDto transactionSuccessful(){
        return new TransactionResponseDto(true, "Transaction Successful");
    }


    public static TransactionResponseDto accountNotFound(){
        return new TransactionResponseDto(false, "Account does not exist");
    }


    public static TransactionResponseDto notEnoughFunds(){
        return new TransactionResponseDto(false, "Not enough funds");
    }


    public static TransactionResponseDto invalidTransaction(){
        return new TransactionResponseDto(false, "Invalid Transaction");
    }

    public static TransactionResponseDto invalidTransaction(String message){
        return new TransactionResponseDto(false, message);
    }


    public static TransactionResponseDto invalidAmount(){
        return new TransactionResponseDto(false, "Invalid amount");
    }

}
