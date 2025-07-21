package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.exception.InvalidTransactionException;
import com.finance_vault.finance_vault.model.currency.Currency;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.TransactionRepository;
import com.finance_vault.finance_vault.repository.UserRepository;
import com.finance_vault.finance_vault.service.notification.NotificationService;
import com.finance_vault.finance_vault.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void ShouldCreateTransaction() {
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
        doNothing().when(notificationService).addTransactionNotification(Mockito.any(Transaction.class));

        // Act
        transactionService.createTransaction(transaction, sender);

        // Assert
        Assertions.assertThat(sender.getBalance()).isEqualTo(900);
        Assertions.assertThat(receiver.getBalance()).isEqualTo(1100);
    }


    @Test
    public void ShouldThrowException_WhenSelfTransaction() {
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

        TransactionRequest request = new TransactionRequest();
        request.setAmount(100);
        request.setReceiverEmail("abc@abc.com");

        // ACT
        when(userService.getUserFromEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userService.getUserFromEmail(request.getReceiverEmail())).thenReturn(Optional.of(sender));

        InvalidTransactionException e = assertThrows(InvalidTransactionException.class,
                () -> transactionService.createTransaction(request, sender));

        Assertions.assertThat(e.getMessage()).contains("You cannot send a transaction to yourself");
    }


    @Test
    public void ShouldThrowException_WhenBalanceIsNotEnough() {
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

        TransactionRequest request = new TransactionRequest();
        request.setAmount(5000);
        request.setReceiverEmail("def@def.com");

        // ACT
        // Mocks the methods within the TransactionService
        when(userService.getUserFromEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userService.getUserFromEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(null);

        InvalidTransactionException e = assertThrows(InvalidTransactionException.class,
                () -> transactionService.createTransaction(request, sender));

        Assertions.assertThat(e.getMessage()).contains("Not enough funds to complete the transaction");
    }


    @Test
    public void ShouldThrowException_WhenTransactionAmountIsNegative() {
        // Arrange
        User sender = User.builder()
                .name("A")
                .lastName("B")
                .email("abc@abc.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        User receiver = User.builder()
                .name("D")
                .lastName("E")
                .email("def@def.com")
                .balance(1000)
                .sentTransactions(new ArrayList<>())
                .receivedTransactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        TransactionRequest request = new TransactionRequest();
        request.setAmount(-400);
        request.setReceiverEmail("def@def.com");

        // Act
        when(userService.getUserFromEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userService.getUserFromEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));


        InvalidTransactionException e = assertThrows(InvalidTransactionException.class,
                () -> transactionService.createTransaction(request, sender));

        // Assert
       Assertions.assertThat(e.getMessage()).contains("Transaction amount should be positive");
    }

    @Test
    public void ShouldThrowException_WhenReceiverEmailIsInvalid() {
        // Arrange
        TransactionRequest request = new TransactionRequest();
        request.setAmount(25);
        request.setReceiverEmail("foo");

        // Act & Assert
        doThrow(InvalidTransactionException.receiverDoesNotExist())
                .when(userService).getUserFromEmail(request.getReceiverEmail());

        InvalidTransactionException e = assertThrows(InvalidTransactionException.class,
                () -> userService.getUserFromEmail(request.getReceiverEmail()));

        Assertions.assertThat(e.getMessage()).contains("Receiver does not exist");
    }
}
