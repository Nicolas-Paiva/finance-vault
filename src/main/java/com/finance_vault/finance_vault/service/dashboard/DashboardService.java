package com.finance_vault.finance_vault.service.dashboard;

import com.finance_vault.finance_vault.dto.dashboard.DashboardDataDTO;
import com.finance_vault.finance_vault.model.user.User;

public interface DashboardService {

    DashboardDataDTO getDashboardData(User user);

}
