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

        List<Double> expenses = transactionService.getUserMonthlyTransactionValuesByEmail(email, TransactionType.WITHDRAWAL);

         List<Double> deposits = transactionService.getUserMonthlyTransactionValuesByEmail(email, TransactionType.DEPOSIT);

        double totalExpenses = sumTransactions(expenses);

        double totalDeposits = sumTransactions(deposits);

        return MonthlyResumeDto.builder()
                .totalExpenses(totalExpenses)
                .totalDeposits(totalDeposits)
                .monthlyBalance(totalDeposits - totalExpenses)
                .build();
    }


    // TODO: Implement averages for long term comparison
    private double calculateAverage(List<Double> values){
        return sumTransactions(values) / values.size();
    }


    private double sumTransactions(List<Double> values){
        double sum = 0;

        for(double value : values){
            sum += value;
        }

        return sum;
    }

}
