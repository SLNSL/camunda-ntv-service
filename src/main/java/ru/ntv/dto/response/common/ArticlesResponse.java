package ru.ntv.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ntv.entity.articles.Article;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesResponse {
    private List<Article> articles;
}
