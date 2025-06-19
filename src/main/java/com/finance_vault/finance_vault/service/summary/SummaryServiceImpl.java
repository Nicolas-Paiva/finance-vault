package com.finance_vault.finance_vault.service.summary;

import com.finance_vault.finance_vault.dto.summary.SummaryDTO;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final TransactionService transactionService;

    @Override
    public SummaryDTO getSummaryData(User user) {
        return SummaryDTO.builder()
                .name(user.getName())
                .balance(user.getBalance())
                .currency(user.getCurrency())
                .monthlyDepositsTotal(transactionService.getMonthlyDepositsTotal(user))
                .monthlyWithdrawalsTotal(transactionService.getMonthlyWithdrawalsTotal(user))
                .build();
    }

}
