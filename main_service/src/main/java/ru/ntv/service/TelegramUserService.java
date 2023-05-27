package ru.ntv.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.articles.Theme;
import ru.ntv.entity.users.TelegramUser;
import ru.ntv.repo.TelegramUserAndThemeRepository;
import ru.ntv.repo.TelegramUserRepository;
import ru.ntv.telegram.TelegramBot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class TelegramUserService {

    @Autowired
    TelegramUserRepository telegramUserRepository;

    @Autowired
    TelegramUserAndThemeRepository telegramUserAndThemeRepository;

    @Autowired
    TelegramBot telegramBot;


    public void sendArticles(Article article) {
        Set<Long> usersGetNew = new HashSet<>();
        AtomicInteger res = new AtomicInteger();
        List<Theme> themesList = article.getThemes();

        themesList.forEach(theme -> {
            telegramUserAndThemeRepository.findByIdThemeId(theme.getId()).forEach(telegramUserAndTheme -> {
                SendMessage sendMessage = new SendMessage();
                TelegramUser telegramUser = telegramUserRepository.findById(telegramUserAndTheme.getId().getTelegramUserId()).get();
                if (usersGetNew.contains(telegramUser.getTelegramChatId())) return;
                sendMessage.setChatId(telegramUser.getTelegramChatId());
                String message = article.toString();
                InputFile photo = new InputFile(article.getPhoto());

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