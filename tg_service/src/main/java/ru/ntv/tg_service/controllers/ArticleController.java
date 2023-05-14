//package ru.ntv.tg_service.controllers;
//
//import javax.validation.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ru.ntv.tg_service.dto.NewArticleRequest;
//import ru.ntv.tg_service.service.ArticleService;
//
//
//@RestController
//@RequestMapping("articles")
//public class ArticleController {
//
//    @Autowired
//    ArticleService articleService;
//
//
//    @GetMapping
//    ResponseEntity<?> createArticle(@Valid @RequestBody NewArticleRequest newArticleRequest){
//        final var response = articleService.sendArticles(newArticleRequest);
//
//        return ResponseEntity.ok(response);
//    }
//
//}