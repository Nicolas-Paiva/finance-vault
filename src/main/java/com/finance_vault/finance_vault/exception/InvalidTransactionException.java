package com.finance_vault.finance_vault.exception;

public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message) {
        super(message);
    }


    public static InvalidTransactionException invalidAmount() {
        return new InvalidTransactionException("Transaction amount should be positve");
    }


    public static InvalidTransactionException notEnoughFunds() {
        return new InvalidTransactionException("Not enough funds to complete the transaction");
    }


    public static InvalidTransactionException senderDoesNotExist() {
        return new InvalidTransactionException("Receiver does not exist");
    }


    public static InvalidTransactionException receiverDoesNotExist() {
        return new InvalidTransactionException("Receiver does not exist");
    }


    public static InvalidTransactionException selfTransactionNotAllowed() {
        return new InvalidTransactionException("You cannot send a transaction to yourself");
    }

}
