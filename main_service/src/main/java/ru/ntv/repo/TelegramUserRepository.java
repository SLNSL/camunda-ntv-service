package ru.ntv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.users.TelegramUser;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Integer> {
    
    Optional<TelegramUser> findByUserId(Integer userId);

    void deleteAllByTelegramName(String telegramName);

    Optional<TelegramUser> findByTelegramName(String telegramName);

}