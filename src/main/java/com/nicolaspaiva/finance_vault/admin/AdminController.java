package com.nicolaspaiva.finance_vault.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    // TODO: Create admin functionality

    @GetMapping("/hello")
    public ResponseEntity<?> helloAdmin(){
        return ResponseEntity.ok("You are an admin");
    }

}
