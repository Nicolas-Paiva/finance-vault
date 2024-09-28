package com.nicolaspaiva.finance_vault.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderImpl implements EmailSender{

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendEmail(String to, String email){
        try{

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(email, true);

            helper.setTo(to);

            helper.setSubject("Confirmation Email - Finance Vault");

            helper.setFrom("nicolaspaiva@outlook.com");

            log.info("Mail Send?????");

            javaMailSender.send(mimeMessage);

            log.info("Mail Send?????");

        } catch (MessagingException e){
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
