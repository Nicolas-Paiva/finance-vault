package com.finance_vault.finance_vault.service.summary;

import com.finance_vault.finance_vault.dto.summary.MonthlyTransactionsDTO;
import com.finance_vault.finance_vault.dto.summary.SummaryDTO;
import com.finance_vault.finance_vault.dto.summary.WeeklyTotalsDto;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.notification.NotificationService;
import com.finance_vault.finance_vault.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final TransactionService transactionService;

    private final NotificationService notificationService;

    @Override
    public SummaryDTO getSummaryData(User user) {
        return SummaryDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .balance(user.getBalance())
                .currency(user.getCurrency())
                .monthlyDepositsTotal(transactionService.getMonthlyDepositsTotal(user))
                .monthlyWithdrawalsTotal(transactionService.getMonthlyWithdrawalsTotal(user))
                .numberOfNotifications(notificationService.getNumberOfUnseenNotifications(user))
                .build();
    }


    @Override
    public MonthlyTransactionsDTO getMonthlyTransactions(User user) {
        return transactionService.getMonthlyTransactions(user);
    }

    public WeeklyTotalsDto getMonthWeeklyTotals(User user) {
        return transactionService.getMonthWeeklyWithdrawals(user);
    }

}
