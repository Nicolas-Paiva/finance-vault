package com.nicolaspaiva.finance_vault.expenses.service;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.repository.BankAccountRepository;
import com.nicolaspaiva.finance_vault.expenses.dto.MonthlyResumeDto;
import com.nicolaspaiva.finance_vault.expenses.dto.TransactionType;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import com.nicolaspaiva.finance_vault.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonthlyExpensesServiceImpl implements MonthlyExpensesService{

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;


    // TODO: Calculate the balance
    @Override
    public MonthlyResumeDto getMonthlyExpensesAndBalance(String email){

        List<Float> expenses = getUserMonthlyTransactionsByEmail(email, TransactionType.WITHDRAWAL);

         List<Float> deposits = getUserMonthlyTransactionsByEmail(email, TransactionType.DEPOSIT);

        float totalExpenses = sumTransactions(expenses);

        float totalDeposits = sumTransactions(deposits);

        return MonthlyResumeDto.builder()
                .totalExpenses(totalExpenses)
                .totalDeposits(totalDeposits)
                .monthlyBalance(totalDeposits - totalExpenses)
                .build();
    }

    /**
     * Returns a list with either withdrawals
     * or deposits
     */
    private List<Float> getUserMonthlyTransactionsByEmail(String email, TransactionType transactionType){

        int userId = getUserIdByEmail(email);

        LocalDateTime firstDayOfTheMonth = getFirstDayOfTheMonth();

        List<TransactionEntity> transactionEntities = new ArrayList<>();


        if(transactionType.equals(TransactionType.WITHDRAWAL)){
             transactionEntities = transactionRepository.findWithdrawalsByIdAndDateRange
                            (userId, firstDayOfTheMonth, LocalDateTime.now());
        } else {
            transactionEntities = transactionRepository.findDepositsByIdAndDateRange
                    (userId, firstDayOfTheMonth, LocalDateTime.now());
        }

        return getAllTransactionValues(transactionEntities);
    }


    /**
     * Gets the user ID by their email.
     * If the user does not exist, an
     * exception is thrown.
     */
    private int getUserIdByEmail(String email){

        Optional<BankAccountEntity> account =
                bankAccountRepository.findBankAccountEntityByOwnerEmail(email);

        if(account.isEmpty()){
            throw new RuntimeException("User does not exist");
        }

        return account.get().getId();
    }


    /**
     * Returns the first day of the month
     */
    private LocalDateTime getFirstDayOfTheMonth(){
        return LocalDateTime.now().withDayOfMonth(1);
    }


    /**
     * Returns a list with all the values from the
     * transaction entities
     */
    private List<Float> getAllTransactionValues(List<TransactionEntity> transactionEntities){
        List<Float> values = new ArrayList<>();

        for(TransactionEntity transactionEntity : transactionEntities){
            values.add(transactionEntity.getAmount());
        }

        return values;
    }


    /**
     * Calculates the average
     */
    private float calculateAverage(List<Float> values){
        return sumTransactions(values) / values.size();
    }


    /**
     * Calculates the total expenses
     */
    private float sumTransactions(List<Float> values){
        float sum = 0;

        for(float value : values){
            sum += value;
        }

        return sum;
    }

}
