package com.nicolaspaiva.finance_vault.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboard {

    long numberOfUsers;

    long monthlyActiveUsers;

    long newMonthlyUsers;

    long monthlyTransactions;

    double monthlyTransactionVolume;

    double monthlyRevenue;
}
