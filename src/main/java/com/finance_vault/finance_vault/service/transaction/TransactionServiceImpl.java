package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.dto.transaction.TransactionResponse;
import com.finance_vault.finance_vault.exception.InvalidTransactionException;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.TransactionRepository;
import com.finance_vault.finance_vault.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest, User user) {
        float amount = transactionRequest.getAmount();

        // Check whether sender exists
            User sender = userService.getUserFromEmail(user.getEmail())
                    .orElseThrow(InvalidTransactionException::senderDoesNotExist);

        // Check whether receiver exists
            User receiver = userService.getUserFromEmail(transactionRequest.getReceiverEmail())
                    .orElseThrow(InvalidTransactionException::receiverDoesNotExist);

        if (sender.getEmail().equals(receiver.getEmail())) {
            throw InvalidTransactionException.selfTransactionNotAllowed();
        }

        // Check whether value is positive
        if (amount <= 0) {
            throw InvalidTransactionException.invalidAmount();
        }

        // Check whether there is enough funds
        if (user.getBalance() < amount) {
            throw InvalidTransactionException.notEnoughFunds();
        }


        // Performs the transaction
        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .build();

        transactionRepository.save(transaction);


        sender.setBalance(sender.getBalance() - amount);
        sender.getSentTransactions().add(transaction);

        receiver.setBalance(receiver.getBalance() + amount);
        receiver.getReceivedTransactions().add(transaction);


        return TransactionResponse.success();
    }

}
