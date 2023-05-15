package ru.ntv.tg_service.dto.kafka;


import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ArticleKafkaDTO implements Serializable {
    private List<ThemeDTO> themes;
    private String header;
    private String subheader;
    private String text;
    private String photoURL;


    @Override
    public String toString(){
        return "<b>" + header + "</b>" + "\n\n" +
                "Темы: " + themes.stream().map(ThemeDTO::getName).collect(Collectors.joining(", ")) + "\n\n" +
                subheader + "\n" +
                text;
    }

}
