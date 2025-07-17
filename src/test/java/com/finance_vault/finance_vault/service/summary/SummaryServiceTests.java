package com.finance_vault.finance_vault.service.summary;

import com.finance_vault.finance_vault.dto.summary.MonthlyTransactionsDTO;
import com.finance_vault.finance_vault.model.currency.Currency;
import com.finance_vault.finance_vault.model.transaction.Transaction;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.TransactionRepository;
import com.finance_vault.finance_vault.repository.UserRepository;
import com.finance_vault.finance_vault.service.transaction.TransactionService;
import com.finance_vault.finance_vault.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class SummaryServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SummaryService summaryService;



    @Test
    public void SummaryServiceShouldReturnMonthlyTransactions() {
        // Arrange
        User receiver = User.builder()
                .name("Nicolas")
                .lastName("Paiva")
                .password("12345678.A")
                .currency(Currency.EUR)
                .email("nicolas@gmail.com")
                .balance(1000)
                .build();

        User sender = User.builder()
                .name("Bartholomeu")
                .lastName("Kuma")
                .password("12345678.A")
                .currency(Currency.EUR)
                .email("kuma@gmail.com")
                .balance(9999)
                .build();

        userRepository.save(receiver);
        userRepository.save(sender);
        userRepository.flush();

        LocalDateTime now = LocalDateTime
                .of(2025, 7, 15, 12, 0);

        Transaction d1 = Transaction.builder()
                .receiver(receiver)
                .sender(sender)
                .amount(10)
                .createdAt(now)
                .build();

        Transaction d2 = Transaction.builder()
                .receiver(receiver)
                .sender(sender)
                .amount(20)
                .createdAt(now.withDayOfMonth(5))
                .build();

        Transaction d3 = Transaction.builder()
                .receiver(receiver)
                .sender(sender)
                .amount(50)
                .createdAt(now.withDayOfMonth(15))
                .build();

        Transaction d4 = Transaction.builder()
                .receiver(receiver)
                .sender(sender)
                .amount(185)
                .createdAt(now.withDayOfMonth(30))
                .build();

        Transaction w1 = Transaction.builder()
                .receiver(sender)
                .sender(receiver)
                .amount(1.55f)
                .createdAt(now.withDayOfMonth(2))
                .build();

        Transaction w2 = Transaction.builder()
                .receiver(sender)
                .sender(receiver)
                .amount(8.55f)
                .createdAt(now.withDayOfMonth(12))
                .build();

        Transaction w3 = Transaction.builder()
                .receiver(sender)
                .sender(receiver)
                .amount(0.55f)
                .createdAt(now.withDayOfMonth(28))
                .build();

        // Transactions that should not be
        // included in the results

        Transaction x1 = Transaction.builder()
                .receiver(receiver)
                .sender(sender)
                .amount(12f)
                .createdAt(now.minusMonths(1))
                .build();

        Transaction x2 = Transaction.builder()
                .receiver(receiver)
                .sender(sender)
                .amount(89.99f)
                .createdAt(now.plusMonths(2))
                .build();

        Transaction x3 = Transaction.builder()
                .receiver(sender)
                .sender(receiver)
                .amount(250f)
                .createdAt(now.plusMonths(5))
                .build();

        transactionRepository.save(d1);
        transactionRepository.save(d2);
        transactionRepository.save(d3);
        transactionRepository.save(d4);
        transactionRepository.save(w1);
        transactionRepository.save(w2);
        transactionRepository.save(w3);
        transactionRepository.save(x1);
        transactionRepository.save(x2);
        transactionRepository.save(x3);
        transactionRepository.flush();


        MonthlyTransactionsDTO monthlyTransactionsDTO = summaryService.getMonthlyTransactions(receiver);

        Assertions.assertThat(monthlyTransactionsDTO.getDeposits().size()).isEqualTo(4);
        Assertions.assertThat(monthlyTransactionsDTO.getWithdrawals().size()).isEqualTo(3);
    }

}
