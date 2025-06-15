package com.finance_vault.finance_vault.controller.dashboard;

import com.finance_vault.finance_vault.dto.dashboard.DashboardDataDTO;
import com.finance_vault.finance_vault.exception.UserNotFoundException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.dashboard.DashboardService;
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
public class DashboardController {

    // TODO: Create interfaces and implement them for other services

    private final UserService userService;

    private final DashboardService dashboardService;


    /**
     * Returns the user's dashboard data, which includes the user's name
     * and current balance.
     *
     * If the user does not exist, a UserNotFoundException is thrown,
     * returning the message to the client.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getSummary(Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
        DashboardDataDTO dashboardDataDTO = dashboardService.getDashboardData(user);

        return ResponseEntity.ok(dashboardDataDTO);
    }

}
