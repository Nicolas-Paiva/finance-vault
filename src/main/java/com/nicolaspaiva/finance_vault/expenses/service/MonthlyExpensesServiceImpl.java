package com.nicolaspaiva.finance_vault.expenses.service;

import com.nicolaspaiva.finance_vault.expenses.dto.MonthlyResumeDto;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionType;
import com.nicolaspaiva.finance_vault.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyExpensesServiceImpl implements MonthlyExpensesService{

    private final TransactionService transactionService;


    /**
     * Calculates the monthly expenses and the
     * balance, returning a MonthlyResumoDto
     * to the client
     */
    @Override
    public MonthlyResumeDto getMonthlyExpensesAndBalance(String email){

        List<Float> expenses = transactionService.getUserMonthlyTransactionValuesByEmail(email, TransactionType.WITHDRAWAL);

         List<Float> deposits = transactionService.getUserMonthlyTransactionValuesByEmail(email, TransactionType.DEPOSIT);

        float totalExpenses = sumTransactions(expenses);

        float totalDeposits = sumTransactions(deposits);

        return MonthlyResumeDto.builder()
                .totalExpenses(totalExpenses)
                .totalDeposits(totalDeposits)
                .monthlyBalance(totalDeposits - totalExpenses)
                .build();
    }


    // TODO: Implement averages for long term comparison
    private float calculateAverage(List<Float> values){
        return sumTransactions(values) / values.size();
    }


    private float sumTransactions(List<Float> values){
        float sum = 0;

        for(float value : values){
            sum += value;
        }

        return sum;
    }

}
