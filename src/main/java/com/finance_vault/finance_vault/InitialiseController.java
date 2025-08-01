package com.finance_vault.finance_vault;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sends a request to the API in order
 * to spin up the server
 */
@RestController
public class InitialiseController {
    @GetMapping("/initialise")
    public String initialise() {
        return "Initialising server...";
    }
}
