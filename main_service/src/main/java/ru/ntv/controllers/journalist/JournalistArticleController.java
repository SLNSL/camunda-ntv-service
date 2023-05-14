package ru.ntv.controllers.journalist;

import javax.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ntv.dto.request.journalist.NewArticleRequest;
import ru.ntv.entity.articles.Article;
import ru.ntv.exception.ArticleNotFoundException;
import ru.ntv.service.ArticleService;

@RestController
@RequestMapping("articles")
@Validated
public class JournalistArticleController {

    @Autowired
    private ArticleService articleService;


    @PostMapping
    ResponseEntity<Article> createArticle(@Valid @RequestBody NewArticleRequest newArticleRequest){
        final var article = articleService.createArticle(newArticleRequest);

        return ResponseEntity.ok(article);
    }

    @DeleteMapping(params = "id")
    ResponseEntity<?> deleteArticle( @RequestParam int id){
        articleService.delete(id);

        return ResponseEntity.ok("OK");
    }


    @PutMapping(params = "id")
    ResponseEntity<Article> updateArticle(
            @RequestParam int id,
            @Valid @RequestBody NewArticleRequest req
    ) throws ArticleNotFoundException {
        final var article = articleService.update(id, req);

        return ResponseEntity.ok(article);
    }
}