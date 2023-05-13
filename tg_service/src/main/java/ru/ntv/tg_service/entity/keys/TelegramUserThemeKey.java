package ru.ntv.tg_service.entity.keys;


import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class TelegramUserThemeKey implements Serializable {
    private String telegramUserName;
    private String themeName;
}
