package com.nicolaspaiva.finance_vault.email;

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
    @Override
    public void sendConfirmationEmail(String to, String email){
        try{

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            buildConfirmationEmail(helper, email, to);

//             javaMailSender.send(mimeMessage);

        } catch (MessagingException e){
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }

    private void buildConfirmationEmail(MimeMessageHelper messageHelper, String email, String to)
            throws MessagingException {
        messageHelper.setText(email, true);
        messageHelper.setTo(to);
        messageHelper.setSubject("Confirm your email - Finance Vault");
        messageHelper.setFrom("nicolaspaiva@outlook.com");
    }
}
