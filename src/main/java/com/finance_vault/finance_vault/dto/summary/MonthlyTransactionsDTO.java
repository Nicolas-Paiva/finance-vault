package com.finance_vault.finance_vault.dto.summary;

import com.finance_vault.finance_vault.dto.transaction.TransactionView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTransactionsDTO {

    private List<TransactionView> deposits;

    private List<TransactionView> withdrawals;
}
