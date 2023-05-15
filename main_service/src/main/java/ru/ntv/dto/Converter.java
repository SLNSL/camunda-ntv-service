package ru.ntv.dto;

import ru.ntv.dto.kafka.ThemeDTO;
import ru.ntv.entity.articles.Theme;

public class Converter {

    public static ThemeDTO themeToDtoConverter(Theme theme){
        ThemeDTO themeDTO = new ThemeDTO();
        themeDTO.setId(theme.getId());
        themeDTO.setName(theme.getThemeName());
        return themeDTO;
    }
}
