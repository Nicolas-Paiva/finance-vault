package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.PaginatedResponse;
import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.dto.transaction.TransactionResponse;
import com.finance_vault.finance_vault.dto.transaction.TransactionView;
import com.finance_vault.finance_vault.exception.InvalidTransactionException;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.TransactionRepository;
import com.finance_vault.finance_vault.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    private final UserService userService;


    /**
     *
     * Creates a transaction between two users.
     * A transaction is only allowed if the sender has
     * enough funds and provide a correct email address
     * for the receiver.
     *
     * If the transaction is successful, a TransactionResponse with
     * success equal to true is sent to the client.
     */
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


    @Override
    public PaginatedResponse<TransactionView> getAllTransactions(int page, int size, User user) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<TransactionView> transactions = transactionRepository.findAllByUser(user, pageable).map((transaction) -> {

            if (transaction.getSender().getEmail().equals(user.getEmail())) {
                return TransactionView.toWithdrawal(transaction);
            }

            return TransactionView.toDeposit(transaction);
        });

        return getPaginatedTransactions(transactions);
    }

    private PaginatedResponse<TransactionView> getPaginatedTransactions(Page<TransactionView> page) {
        return new PaginatedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }


    public float getMonthlyDepositsTotal(User user) {
        List<Transaction> monthlyDeposits = transactionRepository.findAllMonthlyDeposits(user,
                LocalDate.now().withDayOfMonth(1).atStartOfDay(),LocalDateTime.now());

        return monthlyDeposits.stream()
                .map(Transaction::getAmount)
                .reduce(0F, Float::sum);
    }


    public float getMonthlyWithdrawalsTotal(User user) {
        List<Transaction> monthlyWithdrawals = transactionRepository.findAllMonthlyWithdrawals(user,
                LocalDate.now().withDayOfMonth(1).atStartOfDay(),LocalDateTime.now());

        return monthlyWithdrawals.stream()
                .map(Transaction::getAmount)
                .reduce(0F, Float::sum);
    }

}
