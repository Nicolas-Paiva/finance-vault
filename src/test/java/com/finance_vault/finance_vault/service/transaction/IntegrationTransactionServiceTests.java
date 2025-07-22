package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.summary.WeeklyTotalsDto;
import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class IntegrationTransactionServiceTests {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Test
    public void shouldReturnValidWeeklyTotals() {
        // Arrange
        User sender = User.builder()
                .name("Nicolas")
                .lastName("Paiva")
                .email("nicolas@gmail.com")
                .password("12345678.A")
                .currency(Currency.EUR)
                .balance(9999)
                .build();

        User receiver = User.builder()
                .name("Bartholomeu")
                .lastName("Kuma")
                .email("kuma@gmail.com")
                .password("12345678.A")
                .currency(Currency.EUR)
                .balance(9999)
                .build();

        // First week transactions
        Transaction t1 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(1)
                .createdAt(LocalDateTime.parse("2025-07-01T10:15:30"))
                .build();

        Transaction t2 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(1)
                .createdAt(LocalDateTime.parse("2025-07-03T14:20:00"))
                .build();

        Transaction t3 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(1)
                .createdAt(LocalDateTime.parse("2025-07-07T08:45:00"))
                .build();

        // Second week transactions
        Transaction t4 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(5)
                .createdAt(LocalDateTime.parse("2025-07-08T09:00:00"))
                .build();

        Transaction t5 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(5)
                .createdAt(LocalDateTime.parse("2025-07-10T16:10:00"))
                .build();

        Transaction t6 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(5)
                .createdAt(LocalDateTime.parse("2025-07-14T12:30:45"))
                .build();

        // Third week
        Transaction t7 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(10)
                .createdAt(LocalDateTime.parse("2025-07-15T11:00:00"))
                .build();

        Transaction t8 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(15)
                .createdAt(LocalDateTime.parse("2025-07-18T18:25:00"))
                .build();

        Transaction t9 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(20)
                .createdAt(LocalDateTime.parse("2025-07-20T07:45:30"))
                .build();

        // Fourth week
        Transaction t10 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(8)
                .createdAt(LocalDateTime.parse("2025-07-22T10:10:10"))
                .build();

        Transaction t11 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(12)
                .createdAt(LocalDateTime.parse("2025-07-25T13:15:00"))
                .build();

        Transaction t12 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(18)
                .createdAt(LocalDateTime.parse("2025-07-30T21:00:00"))
                .build();

        // Transactions that should bot be included
        Transaction t13 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(50)
                .createdAt(LocalDateTime.parse("2026-07-15T12:00:00"))
                .build();

        Transaction t14 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(60)
                .createdAt(LocalDateTime.parse("2025-08-01T09:30:00"))
                .build();

        Transaction t15 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(70)
                .createdAt(LocalDateTime.parse("2025-08-10T15:45:00"))
                .build();

        Transaction t16 = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(80)
                .createdAt(LocalDateTime.parse("2025-06-25T16:20:00"))
                .build();

        userRepository.save(sender);
        userRepository.save(receiver);
        userRepository.flush();

        transactionRepository.save(t1);
        transactionRepository.save(t2);
        transactionRepository.save(t3);
        transactionRepository.save(t4);
        transactionRepository.save(t5);
        transactionRepository.save(t6);
        transactionRepository.save(t7);
        transactionRepository.save(t8);
        transactionRepository.save(t9);
        transactionRepository.save(t10);
        transactionRepository.save(t11);
        transactionRepository.save(t12);
        transactionRepository.save(t13);
        transactionRepository.save(t14);
        transactionRepository.save(t15);
        transactionRepository.save(t16);
        transactionRepository.flush();

        // Act
        WeeklyTotalsDto totalsDto = transactionService.getMonthWeeklyWithdrawals(sender);

        Assertions.assertThat(totalsDto.getTotals().get(0)).isEqualTo(3);
        Assertions.assertThat(totalsDto.getTotals().get(1)).isEqualTo(15);
        Assertions.assertThat(totalsDto.getTotals().get(2)).isEqualTo(45);
        Assertions.assertThat(totalsDto.getTotals().get(3)).isEqualTo(38);
    }

}
