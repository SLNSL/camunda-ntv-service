package ru.ntv.mail_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ntv.mail_service.dto.kafka.ArticleKafkaDTO;
import ru.ntv.mail_service.dto.kafka.ThemeDTO;
import ru.ntv.mail_service.repo.EmailUserRepository;
import ru.ntv.mail_service.repo.EmailUserThemeRepository;

import java.util.*;

@Service
public class ArticleService {
    
    private List<ArticleKafkaDTO> storage = new ArrayList<>();
    
    @Autowired
    private EmailUserRepository emailUserRepository;

    @Autowired
    private EmailUserThemeRepository emailUserThemeRepository;

    @Autowired
    private EmailService emailService;

    @KafkaListener(id = "mailGroup", topics = "article-topic")
    public void receiveArticles(ArticleKafkaDTO articleKafkaDTO) {
        System.out.println(articleKafkaDTO);
        storage.add(articleKafkaDTO);
    }
    

    //@Scheduled(cron = "0 0 23 * * *")
    @Scheduled(fixedRate = 60 * 1000)
    public void sendDigest(){
        if (storage.size() == 0) return;
        HashMap<Integer, List<ArticleKafkaDTO>> articlesByThemeIds = new HashMap<>();
        HashMap<Integer, Set<ArticleKafkaDTO>> articlesByUserId = new HashMap<>();
        
        storage.forEach(article -> {
            final var themeIds = article
                    .getThemes()
                    .stream()
                    .map(ThemeDTO::getId)
                    .toList();
            
            themeIds.forEach(themeId -> {
                if (!articlesByThemeIds.containsKey(themeId)){
                    articlesByThemeIds.put(themeId, new ArrayList<>());
                }
                articlesByThemeIds.get(themeId).add(article);
            });
        });
        
        emailUserThemeRepository.findAll().forEach(subscription -> {
            final var userId = subscription.getUserId();
            final var themeId = subscription.getThemeId();
            
            if (!articlesByUserId.containsKey(userId)){
                articlesByUserId.put(userId, new HashSet<>());
            }
            System.out.println(userId);
            System.out.println(themeId);
            System.out.println(articlesByUserId.get(userId));
            System.out.println(articlesByThemeIds.get(themeId));
            articlesByUserId.get(userId).addAll(articlesByThemeIds.get(themeId));
        });

        articlesByUserId.forEach((userId, articles) -> 
                emailService.sendArticles(
                        emailUserRepository.findByUserId(userId).getEmail(),
                        articles
                )
        );

        storage.clear();
    }
}