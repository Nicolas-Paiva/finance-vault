package com.finance_vault.finance_vault.dto.summary;

import com.finance_vault.finance_vault.model.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTransactionsDTO {

    private List<Transaction> deposits;

    private List<Transaction> withdrawals;
}
