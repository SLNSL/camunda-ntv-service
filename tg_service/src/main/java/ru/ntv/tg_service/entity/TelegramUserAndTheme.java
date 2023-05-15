package ru.ntv.tg_service.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntv.tg_service.entity.keys.TelegramUserThemeKey;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "telegram_user_theme")
public class TelegramUserAndTheme {
    @EmbeddedId
    private TelegramUserThemeKey id;
}