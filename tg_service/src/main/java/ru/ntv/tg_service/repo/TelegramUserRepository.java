package ru.ntv.tg_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.tg_service.entity.TelegramUser;
import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Integer> {
    Optional<TelegramUser> findByTelegramName(String telegramName);
}