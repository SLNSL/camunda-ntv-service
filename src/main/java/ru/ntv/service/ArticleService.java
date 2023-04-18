package ru.ntv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.ntv.dto.request.journalist.NewArticleRequest;
import ru.ntv.entity.users.User;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.exception.ArticleNotFoundException;
import ru.ntv.dto.response.common.ArticlesResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.repo.article.ArticleRepository;
import ru.ntv.repo.article.ThemeRepository;
import ru.ntv.repo.user.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ThemeRepository themeRepository;


    @Autowired
    private UserRepository userRepository;

    public Optional<List<Article>> findByHeader(String header){
        return articleRepository.findAllByHeaderContainingIgnoreCase(header);
    }

    public List<Article> getArticlesByThemes(List<Integer> theme_ids) {
        final var themes = themeRepository.findAllById(theme_ids);

        return articleRepository.findByThemesIn(themes);
    }

    public Optional<Article> findById(int id){
        return articleRepository.findById(id);
    }

    public Article createArticle(NewArticleRequest newArticleRequest) {
        return articleRepository.save(
                convertNewArticleRequestToArticle(newArticleRequest)
        );
    }

    public ArticlesResponse getAll(Integer offset, Integer limit){
        final var res = new ArticlesResponse();
        res.setArticles(articleRepository.findAll(PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "creationDate"))).get().toList());

        return res;
    }

    public Article update(int id, NewArticleRequest req) throws ArticleNotFoundException{
        final var oldArticleOptional = articleRepository.findById(id);
        if (oldArticleOptional.isEmpty()) throw new ArticleNotFoundException("Article not found!");

        final var article = oldArticleOptional.get();

        if (req.getText() != null) article.setText(req.getText());
        if (req.getHeader() != null) article.setHeader(req.getHeader());
        if (req.getSubheader() != null) article.setSubheader(req.getSubheader());
        if (req.getPhotoURL() != null) article.setPhoto(req.getPhotoURL());
        if (req.getPriority() != null) article.setPriority(req.getPriority());
        if (req.getJournalistId() != null) article.setPriority(req.getJournalistId());

        if (req.getThemeIds() != null) {
            final var themes = themeRepository.findAllById(req.getThemeIds());
            article.setThemes(themes);
        }

        return articleRepository.save(article);
    }


    public void delete(int id){
        articleRepository.deleteById(id);
    }

    private Article convertNewArticleRequestToArticle(NewArticleRequest newArticleRequest){
        final var article = new Article();
        final var themes = themeRepository.findAllById(newArticleRequest.getThemeIds());

        article.setThemes(themes);
        article.setHeader(newArticleRequest.getHeader());
        article.setSubheader(newArticleRequest.getSubheader());
        article.setText(newArticleRequest.getText());
        article.setPriority(newArticleRequest.getPriority());
        article.setPhoto(newArticleRequest.getPhotoURL());
        article.setJournalistId(newArticleRequest.getJournalistId());

        return article;
    }



    public List<Article> getArticlesByJournalistId(int id) {
        var journalist = userRepository.findById(id).get(); //todo throw custom Exception if user is not found

        if (!Objects.equals(journalist.getRole().getRoleName(), DatabaseRole.ROLE_JOURNALIST.name())) throw new RuntimeException(); //todo throw custom Exception that isn't boss

        return articleRepository.findByJournalistId(journalist.getId());
    }
}