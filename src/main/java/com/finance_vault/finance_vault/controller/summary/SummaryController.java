package com.finance_vault.finance_vault.controller.summary;

import com.finance_vault.finance_vault.dto.summary.SummaryDTO;
import com.finance_vault.finance_vault.dto.summary.WeeklyTotalsDto;
import com.finance_vault.finance_vault.exception.UserNotFoundException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.summary.SummaryService;
import com.finance_vault.finance_vault.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint designed to provide
 * access to user related data,
 * such as name, balance and transactions.
 */
@RestController
@RequiredArgsConstructor
public class SummaryController {

    // TODO: Create interfaces and implement them for other services

    private final UserService userService;

    private final SummaryService summaryService;


    /**
     * Returns the user's dashboard data, which includes the user's name
     * and current balance.
     *
     * If the user does not exist, a UserNotFoundException is thrown,
     * returning the message to the client.
     */
    @GetMapping("/summary")
    public ResponseEntity<SummaryDTO> getSummary(Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
        SummaryDTO summaryDTO = summaryService.getSummaryData(user);
        return ResponseEntity.ok(summaryDTO);
    }

    @GetMapping("/summary/transactions")
    public ResponseEntity<WeeklyTotalsDto> getMonthlyTransactions(Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
        WeeklyTotalsDto weeklyTotals = summaryService.getMonthWeeklyTotals(user);
        return ResponseEntity.ok(weeklyTotals);
    }

}
