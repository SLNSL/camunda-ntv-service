package ru.ntv.dto.kafka;


import lombok.Data;
import ru.ntv.entity.articles.Theme;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleKafkaDTO implements Serializable {
    private List<ThemeDTO> themes;
    private String header;
    private String subheader;
    private String text;
    private String photoURL;

}
