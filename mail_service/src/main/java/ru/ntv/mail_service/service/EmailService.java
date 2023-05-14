package ru.ntv.mail_service.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@NoArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromUser;

    @Autowired
    private JavaMailSender javaMailSender;
    
    private static final String EMAIL_SUBJECT = "НТВ. Актуальные новости.";

    @PostConstruct
    public void sendTestEmail() {
        String email = "Sitkevich2002@mail.ru";
        String message = "Привет, <b>" + email + "</b>";
        send(email, message);
    }

    @Async
    void send(String to, String message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setFrom(fromUser);
            helper.setTo(to);
            helper.setSubject(EMAIL_SUBJECT);
            helper.setText(message, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | MailSendException e) {
            throw new IllegalStateException("Failed to send email to " + to);
        }
    }
}