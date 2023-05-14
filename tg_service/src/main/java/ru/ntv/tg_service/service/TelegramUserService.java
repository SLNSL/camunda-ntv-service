package ru.ntv.tg_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntv.tg_service.entity.TelegramUser;
import ru.ntv.tg_service.repo.TelegramUserRepository;

@Service
public class TelegramUserService {

    @Autowired
    TelegramUserRepository telegramUserRepository;
    
    public void saveChatId(String telegramName, Long chatId){
        TelegramUser user = telegramUserRepository
                .findByTelegramName(telegramName)
                .get();
        
        user.setTelegramChatId(chatId);
        telegramUserRepository.save(user);
    }
}