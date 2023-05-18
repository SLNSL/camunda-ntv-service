package ru.ntv.mail_service.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.ntv.mail_service.dto.kafka.ArticleKafkaDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromUser;

    @Autowired
    private JavaMailSender javaMailSender;
    
    private static final String EMAIL_SUBJECT = "НТВ. Актуальные новости.";

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
    
    public String composeEmail(Collection<ArticleKafkaDTO> articles){
        String email = articles.stream().map(article -> (
            "<div>" +
                "<h4>" + article.getHeader() + "</h4>" +
                "<h5>" + article.getSubheader() + "</h4>" +
                "<p>" + article.getText() + "</p>" +
                "<img src=\"" + article.getPhotoURL() + "\" style=\"max-width:250px;width:100%\">" +
            "</div>")).collect(Collectors.joining());
        email = "<div>" + email + "</div>";
        
        return email;
    }
    
    @Async
    public void sendArticles(String to, Collection<ArticleKafkaDTO> articles){
        var email = composeEmail(articles);
        send(to, email);
    }
}