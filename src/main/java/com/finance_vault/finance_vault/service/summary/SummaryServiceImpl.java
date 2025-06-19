package com.finance_vault.finance_vault.service.summary;

import com.finance_vault.finance_vault.dto.summary.SummaryDTO;
import com.finance_vault.finance_vault.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {


    @Override
    public SummaryDTO getDashboardData(User user) {
        return new SummaryDTO(user.getName(), user.getBalance());
    }

}
