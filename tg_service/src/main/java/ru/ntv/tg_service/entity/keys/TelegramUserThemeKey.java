package ru.ntv.tg_service.entity.keys;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class TelegramUserThemeKey implements Serializable {
    @Column(name = "telegram_user_id")
    private Integer telegramUserId;
    @Column(name = "theme_id")
    private Integer themeId;
}