package com.nicolaspaiva.finance_vault.email;

public interface EmailSender {

    void sendConfirmationEmail(String to, String email);
}
