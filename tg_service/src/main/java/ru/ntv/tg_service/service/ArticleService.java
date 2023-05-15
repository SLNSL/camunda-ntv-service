package ru.ntv.tg_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ntv.tg_service.dto.kafka.ArticleKafkaDTO;
import ru.ntv.tg_service.dto.kafka.ThemeDTO;
import ru.ntv.tg_service.entity.TelegramUser;
import ru.ntv.tg_service.repo.TelegramUserAndThemeRepository;
import ru.ntv.tg_service.repo.TelegramUserRepository;
import ru.ntv.tg_service.telegram.TelegramBot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ArticleService {

    @Autowired
    TelegramUserRepository telegramUserRepository;

    @Autowired
    TelegramUserAndThemeRepository telegramUserAndThemeRepository;

    @Autowired
    TelegramBot telegramBot;


    @KafkaListener(id = "tgGroup", topics = "article-topic")
    public void sendArticles(ArticleKafkaDTO articleKafkaDTO) {
        System.out.println(articleKafkaDTO.getThemes());
        Set<Long> usersGetNew = new HashSet<>();
        AtomicInteger res = new AtomicInteger();
        List<ThemeDTO> themesList = articleKafkaDTO.getThemes();
        
        themesList.forEach(theme -> {
            telegramUserAndThemeRepository.findByIdThemeId(theme.getId()).forEach(telegramUserAndTheme -> {
                SendMessage sendMessage = new SendMessage();
                TelegramUser telegramUser = telegramUserRepository.findById(telegramUserAndTheme.getId().getTelegramUserId()).get();
                if (usersGetNew.contains(telegramUser.getTelegramChatId())) return;
                sendMessage.setChatId(telegramUser.getTelegramChatId());
                String message = articleKafkaDTO.toString();
                InputFile photo = new InputFile(articleKafkaDTO.getPhotoURL());
                
                SendPhoto sendPhoto = new SendPhoto(telegramUser.getTelegramChatId().toString(), photo);
                sendPhoto.setCaption(message);
                sendPhoto.setParseMode(ParseMode.HTML);
                try {
                    telegramBot.execute(sendPhoto);
                    res.getAndIncrement();
                    usersGetNew.add(telegramUser.getTelegramChatId());
                } catch (TelegramApiException e){
                    e.printStackTrace();
                }
            });
        });
    }
}