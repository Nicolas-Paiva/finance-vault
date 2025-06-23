package com.finance_vault.finance_vault.dto.summary;

import com.finance_vault.finance_vault.model.currency.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryDTO {

    private String name;

    private float balance;

    private Currency currency;

    private float monthlyDepositsTotal;

    private float monthlyWithdrawalsTotal;

    private int numberOfNotifications;

}
