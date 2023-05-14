package ru.ntv.entity.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntv.entity.users.keys.TelegramUserThemeKey;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "telegram_user_theme")
public class TelegramUserAndTheme {
    @EmbeddedId
    private TelegramUserThemeKey id;
}