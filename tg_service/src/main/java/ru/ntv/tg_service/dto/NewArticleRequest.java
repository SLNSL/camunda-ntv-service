package ru.ntv.tg_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class NewArticleRequest {

    private List<String> themes;

    private String header;

    private String subheader;

    private String text;

    private Integer priority;

    private String photoURL;

    @Override
    public String toString(){
        return header.toUpperCase() + "\n\n" + subheader + "\n\n" + text;
    }
}