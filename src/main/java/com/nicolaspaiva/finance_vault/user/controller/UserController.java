package com.nicolaspaiva.finance_vault.user.controller;

import com.nicolaspaiva.finance_vault.expenses.service.MonthlyExpensesService;
import com.nicolaspaiva.finance_vault.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserAccountService userAccountService;

    private final MonthlyExpensesService monthlyExpensesService;


    @GetMapping("/account")
    public ResponseEntity<?> getUserAccount(Principal principal){
        return new ResponseEntity<>(userAccountService.getUserProfile(principal.getName()), HttpStatus.OK);
    }


    // TODO: Currently under development
    @GetMapping("/account/monthly-expenses")
    public ResponseEntity<?> getMonthlyExpenses(Principal principal){
        return new ResponseEntity<>(monthlyExpensesService.getMonthlyExpensesAndBalance(principal.getName()),
                HttpStatus.OK);
    }

}
