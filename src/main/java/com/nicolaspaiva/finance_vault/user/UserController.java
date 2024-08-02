package com.nicolaspaiva.finance_vault.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping("/hello")
    public ResponseEntity<?> helloUser(){
        return ResponseEntity.ok("You are a user");
    }
}
