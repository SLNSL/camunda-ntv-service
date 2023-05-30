package ru.ntv.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntv.entity.articles.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesResponse {
    private List<Article> articles;

    @Override
    public String toString(){
        return articles.stream()
                .map(a -> a.toString(false) + "\n\n")
                .collect(Collectors.joining());
    }
}
