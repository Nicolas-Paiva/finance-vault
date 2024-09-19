package com.nicolaspaiva.finance_vault.bankaccount.controller;

import com.nicolaspaiva.finance_vault.bankaccount.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class BankAccountController {

    // TODO: Create account creation service

    private final BankAccountService bankAccountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(Authentication authentication){
        return ResponseEntity.ok(authentication.getName());
    }

}
