package com.nicolaspaiva.finance_vault.user.controller;

import com.nicolaspaiva.finance_vault.user.dto.UserAccountDto;
import com.nicolaspaiva.finance_vault.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserAccountService userAccountService;


    @GetMapping("/account")
    public ResponseEntity<?> getUserAccount(Principal principal){
        return new ResponseEntity<>(userAccountService.getUserProfile(principal), HttpStatus.OK);
    }

}
