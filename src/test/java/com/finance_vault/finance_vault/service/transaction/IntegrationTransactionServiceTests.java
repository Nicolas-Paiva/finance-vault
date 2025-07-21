package com.finance_vault.finance_vault.service.transaction;

import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.model.currency.Currency;
import com.finance_vault.finance_vault.model.user.User;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class IntegrationTransactionServiceTests {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserRepository userRepository;


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

        userRepository.save(sender);
        userRepository.save(receiver);
        userRepository.flush();


        TransactionRequest r1 = new TransactionRequest();
        r1.setReceiverEmail(sender.getEmail());
        r1.setReceiverEmail(receiver.getEmail());
        r1.setAmount(1);

        transactionService.createTransaction(r1, sender);

        System.out.println(transactionService.getMonthlyWithdrawalsTotal(sender));
    }

}
