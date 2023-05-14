package ru.ntv.tg_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ntv.tg_service.dto.NewArticleRequest;
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

    public String sendArticles(NewArticleRequest newArticleRequest) {
        Set<Long> usersGetNew = new HashSet<>();
        AtomicInteger res = new AtomicInteger();
        List<String> themesList = newArticleRequest.getThemes();
        
        themesList.forEach(theme -> {
            telegramUserAndThemeRepository.findByIdThemeName(theme).forEach(telegramUserAndTheme -> {
                SendMessage sendMessage = new SendMessage();
                TelegramUser telegramUser = telegramUserRepository.findByTelegramName(telegramUserAndTheme.getId().getTelegramUserName()).get();
                if (usersGetNew.contains(telegramUser.getTelegramChatId())) return;
                sendMessage.setChatId(telegramUser.getTelegramChatId());
                String message = newArticleRequest.toString();
                InputFile photo = new InputFile(newArticleRequest.getPhotoURL());
                
                SendPhoto sendPhoto = new SendPhoto(telegramUser.getTelegramChatId().toString(), photo);
                sendPhoto.setCaption(message);
                try {
                    telegramBot.execute(sendPhoto);
                    res.getAndIncrement();
                    usersGetNew.add(telegramUser.getTelegramChatId());
                } catch (TelegramApiException e){
                    e.printStackTrace();
                }
            });
        });
        return "Отправлено " + res + " новостей";
    }
}