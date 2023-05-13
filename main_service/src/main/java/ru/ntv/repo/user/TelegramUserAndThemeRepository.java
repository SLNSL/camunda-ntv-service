package ru.ntv.repo.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.users.TelegramUserAndTheme;
import ru.ntv.entity.users.keys.TelegramUserThemeKey;

@Repository
public interface TelegramUserAndThemeRepository extends JpaRepository<TelegramUserAndTheme, TelegramUserThemeKey> {
}
