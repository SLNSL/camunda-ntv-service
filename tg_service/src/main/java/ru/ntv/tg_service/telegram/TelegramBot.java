package ru.ntv.tg_service.telegram;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ntv.tg_service.service.TelegramUserService;

import java.util.Objects;

@Component
public class TelegramBot extends TelegramLongPollingBot{

    @Autowired
    TelegramUserService userService;

    @Value("${bot.name}")
    private String botUsername;

    public TelegramBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();

        String username = update.getMessage().getFrom().getUserName();
        String messageFromUser = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();


        if (Objects.equals(messageFromUser, "/start")){
            userService.saveChatId(username, chatId);
        }


        sendMessage.setChatId(chatId);
        sendMessage.setText("Для начала работы с ботом необходимо перейти по ссылке http://localhost:21412/themes/subscribe " +
                "и подписаться на рассылку новостей, с определенной темой");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }


}