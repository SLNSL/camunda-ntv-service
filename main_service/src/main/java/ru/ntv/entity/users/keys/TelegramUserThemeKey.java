package ru.ntv.entity.users.keys;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class TelegramUserThemeKey implements Serializable {
    @Column(name = "telegram_user_name")
    private String telegramUserName;
    @Column(name = "theme_name")
    private String themeName;
}