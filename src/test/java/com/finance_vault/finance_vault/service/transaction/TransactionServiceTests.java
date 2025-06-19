package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.exception.InvalidTransactionException;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.TransactionRepository;
import com.finance_vault.finance_vault.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    public void TransactionService_CreateTransaction() {
        // Arrange
        User sender = User.builder()
                .id(1L)
                .name("A")
                .lastName("B")
                .email("abc@abc.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        User receiver = User.builder()
                .id(2L)
                .name("D")
                .lastName("E")
                .email("def@def.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        TransactionRequest transaction = new TransactionRequest();
        transaction.setAmount(100);
        transaction.setReceiverEmail("def@def.com");

        // Mocks the methods within the TransactionService
        when(userService.getUserFromEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userService.getUserFromEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(null);

        // Act
        transactionService.createTransaction(transaction, sender);

        // Assert
        Assertions.assertThat(sender.getBalance()).isEqualTo(900);
        Assertions.assertThat(receiver.getBalance()).isEqualTo(1100);
    }


    @Test(expected = InvalidTransactionException.class)
    public void TransactionService_ThrowException_WhenSelfTransaction() {
        // Arrange
        User sender = User.builder()
                .id(1L)
                .name("A")
                .lastName("B")
                .email("abc@abc.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        User receiver = User.builder()
                .id(2L)
                .name("D")
                .lastName("E")
                .email("abc@abc.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        TransactionRequest transaction = new TransactionRequest();
        transaction.setAmount(100);
        transaction.setReceiverEmail("def@def.com");

        // ACT
        // Mocks the methods within the TransactionService
        when(userService.getUserFromEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userService.getUserFromEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(null);

        transactionService.createTransaction(transaction, sender);
    }


    @Test(expected = InvalidTransactionException.class)
    public void TransactionService_ThrowException_WhenBalanceIsNotEnough() {
        // Arrange
        User sender = User.builder()
                .id(1L)
                .name("A")
                .lastName("B")
                .email("abc@abc.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        User receiver = User.builder()
                .id(2L)
                .name("D")
                .lastName("E")
                .email("def@def.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        TransactionRequest transaction = new TransactionRequest();
        transaction.setAmount(5000);
        transaction.setReceiverEmail("def@def.com");

        // ACT
        // Mocks the methods within the TransactionService
        when(userService.getUserFromEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userService.getUserFromEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(null);

        transactionService.createTransaction(transaction, sender);
    }


    @Test(expected = InvalidTransactionException.class)
    public void TransactionService_ThrowException_WhenTransactionAmountIsNegative() {
        // Arrange
        User sender = User.builder()
                .id(1L)
                .name("A")
                .lastName("B")
                .email("abc@abc.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        User receiver = User.builder()
                .id(2L)
                .name("D")
                .lastName("E")
                .email("def@def.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        TransactionRequest transaction = new TransactionRequest();
        transaction.setAmount(-400);
        transaction.setReceiverEmail("def@def.com");

        // ACT
        // Mocks the methods within the TransactionService
        when(userService.getUserFromEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userService.getUserFromEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(null);

        transactionService.createTransaction(transaction, sender);
    }
}
