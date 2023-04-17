package ru.ntv.controllers.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ntv.dto.response.common.ArticleListResponse;
import ru.ntv.dto.response.common.ArticleResponse;
import ru.ntv.dto.response.common.ArticlesResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.service.ArticleService;
import javax.validation.constraints.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(params = "header")
    ResponseEntity<ArticlesResponse> getArticlesByHeader( @RequestParam String header){
        final var res = new ArticlesResponse();

        Optional<List<Article>> optionalArticles = articleService.findByHeader(header);
        optionalArticles.ifPresent(res::setArticles);

        return ResponseEntity.ok(res);
    }

    @GetMapping(params = "theme_ids")
    ResponseEntity<ArticleListResponse> getArticlesByThemeIds( @RequestParam List<Integer> theme_ids){
        final var articles = articleService.getArticlesByThemes(theme_ids);

        return ResponseEntity.ok(new ArticleListResponse(articles));
    }

    @GetMapping(params = "id")
    ResponseEntity<ArticleResponse> getArticleById( @RequestParam int id){
        final var res = new ArticleResponse();

        Optional<Article> optionalArticle = articleService.findById(id);
        optionalArticle.ifPresent(res::setArticle);

        return ResponseEntity.ok(res);
    }

    @GetMapping(params = {"offset", "limit"})
    ResponseEntity<ArticlesResponse> getAllArticles(
            @RequestParam("offset") @Min(0) Integer offset,
            @RequestParam("limit") @Min(1) @Max(100) Integer limit
    ){
        final var articlesResponse = articleService.getAll(offset, limit);

        return ResponseEntity.ok(articlesResponse);
    }
}