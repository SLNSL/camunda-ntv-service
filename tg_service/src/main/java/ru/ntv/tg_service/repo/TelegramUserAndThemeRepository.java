package ru.ntv.tg_service.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.tg_service.entity.TelegramUserAndTheme;
import ru.ntv.tg_service.entity.keys.TelegramUserThemeKey;

import java.util.List;
import java.util.Optional;


@Repository
public interface TelegramUserAndThemeRepository extends JpaRepository<TelegramUserAndTheme, TelegramUserThemeKey> {

    List<TelegramUserAndTheme> findByIdThemeName(String themeName);
}
