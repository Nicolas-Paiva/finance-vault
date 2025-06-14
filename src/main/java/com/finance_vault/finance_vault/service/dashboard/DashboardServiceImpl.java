package com.finance_vault.finance_vault.service.dashboard;

import com.finance_vault.finance_vault.dto.dashboard.DashboardDataDTO;
import com.finance_vault.finance_vault.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{


    @Override
    public DashboardDataDTO getDashboardData(User user) {
        return new DashboardDataDTO(user.getName(), user.getBalance());
    }

}
