package com.finance_vault.finance_vault.service.summary;

import com.finance_vault.finance_vault.dto.summary.SummaryDTO;
import com.finance_vault.finance_vault.model.user.User;

public interface SummaryService {

    SummaryDTO getDashboardData(User user);

}
