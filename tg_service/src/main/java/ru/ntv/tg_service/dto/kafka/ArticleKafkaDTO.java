package ru.ntv.tg_service.dto.kafka;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleKafkaDTO implements Serializable {
    private List<String> themes;
    private String header;
    private String subheader;
    private String text;
    private String photoURL;


    @Override
    public String toString(){
        return "<b>" + header + "</b>" + "\n\n" +
                "Темы: " + String.join(", ", themes) + "\n\n" +
                subheader + "\n" +
                text;
    }

}
