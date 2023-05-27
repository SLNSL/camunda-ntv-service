package ru.ntv.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ntv.entity.users.TelegramUserAndTheme;
import ru.ntv.entity.users.keys.TelegramUserThemeKey;

import java.util.List;

@Repository
public interface TelegramUserAndThemeRepository extends JpaRepository<TelegramUserAndTheme, TelegramUserThemeKey> {

    List<TelegramUserAndTheme> findByIdThemeId(Integer themeId);

}
