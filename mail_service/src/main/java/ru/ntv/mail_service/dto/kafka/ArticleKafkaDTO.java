package ru.ntv.mail_service.dto.kafka;

import lombok.Data;
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