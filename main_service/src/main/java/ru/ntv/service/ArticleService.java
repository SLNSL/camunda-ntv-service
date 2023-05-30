package ru.ntv.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ntv.dto.Converter;
import ru.ntv.dto.kafka.ArticleKafkaDTO;
import ru.ntv.dto.request.journalist.NewArticleRequest;
import ru.ntv.entity.articles.Theme;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.exception.ArticleNotFoundException;
import ru.ntv.dto.response.common.ArticlesResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.repo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ThemeRepository themeRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUserThemeRepository emailUserThemeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailUserRepository emailUserRepository;

    public Optional<List<Article>> findByHeader(String header){
        return articleRepository.findAllByHeaderContainingIgnoreCase(header);
    }

    public List<Article> getArticlesByThemes(List<Integer> theme_ids) {
        final var themes = themeRepository.findAllById(theme_ids);

        return articleRepository.findByThemesIn((Collection<Theme>) themes);
    }

    public Optional<Article> findById(int id){
        return articleRepository.findById(id);
    }

    public Article createArticle(NewArticleRequest newArticleRequest) {

        Article article = convertNewArticleRequestToArticle(newArticleRequest);
        article = articleRepository.save(article);



        return article;
    }

    public ArticlesResponse getAll(Integer offset, Integer limit){
        final var res = new ArticlesResponse();
        res.setArticles(articleRepository.findAll(PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "creationDate"))).get().toList());
        return res;
    }

//    @Transactional
    public Article update(int id, NewArticleRequest req) throws ArticleNotFoundException{
        final var oldArticleOptional = articleRepository.findById(id);
        if (oldArticleOptional.isEmpty()) throw new ArticleNotFoundException("Article not found!");

        final var article = oldArticleOptional.get();

        if (req.getText() != null) article.setText(req.getText());
        if (req.getHeader() != null) article.setHeader(req.getHeader());
        if (req.getSubheader() != null) article.setSubheader(req.getSubheader());
        if (req.getPhotoURL() != null) article.setPhoto(req.getPhotoURL());
        if (req.getPriority() != null) article.setPriority(req.getPriority());

        if (req.getThemeIds() != null) {
            final var themes = themeRepository.findAllById(req.getThemeIds());
            article.setThemes((List<Theme>) themes);
        }

        return articleRepository.save(article);
    }


    
    public void delete(int id){
        articleRepository.deleteById(id);
    }

    private Article convertNewArticleRequestToArticle(NewArticleRequest newArticleRequest){
        final var article = new Article();
        final var themes = themeRepository.findAllById(newArticleRequest.getThemeIds());

        article.setThemes((List<Theme>) themes);
        article.setHeader(newArticleRequest.getHeader());
        article.setSubheader(newArticleRequest.getSubheader());
        article.setText(newArticleRequest.getText());
        article.setPriority(newArticleRequest.getPriority());
        article.setPhoto(newArticleRequest.getPhotoURL());
        article.setJournalistName(newArticleRequest.getJournalistName());

        return article;
    }



    public List<Article> getArticlesByJournalistName(String name) {
        var journalist = userRepository.findByLogin(name).get(); //todo throw custom Exception if user is not found

        if (!Objects.equals(journalist.getRole().getRoleName(), DatabaseRole.ROLE_JOURNALIST.name())) throw new RuntimeException(); //todo throw custom Exception that isn't boss

        return articleRepository.findAllByJournalistName(journalist.getLogin());
    }

    public List<Article> getArticlesForToday() {
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime endDateTime = LocalDateTime.now();

        return articleRepository.findAllByCreationDateBetween(startDateTime, endDateTime);
    }


    @Scheduled(fixedRate = 30 * 1000)
    public void sendDigest(){

        log.error("ОТПРАВКА");
        List<Article> storage = getArticlesForToday();
        System.out.println(storage.size());
        if (storage.size() == 0) return;
        HashMap<Integer, List<Article>> articlesByThemeIds = new HashMap<>();
        HashMap<Integer, Set<Article>> articlesByUserId = new HashMap<>();

        storage.forEach(article -> {
            final var themeIds = article
                    .getThemes()
                    .stream()
                    .map(Theme::getId)
                    .toList();

            themeIds.forEach(themeId -> {
                if (!articlesByThemeIds.containsKey(themeId)){
                    articlesByThemeIds.put(themeId, new ArrayList<>());
                }
                articlesByThemeIds.get(themeId).add(article);
            });
        });

        emailUserThemeRepository.findAll().forEach(subscription -> {
            final var userId = subscription.getUserId();
            final var themeId = subscription.getThemeId();

            if (!articlesByUserId.containsKey(userId)){
                articlesByUserId.put(userId, new HashSet<>());
            }
            System.out.println(userId);
            System.out.println(themeId);
            System.out.println(articlesByUserId.get(userId));
            System.out.println(articlesByThemeIds.get(themeId));
            articlesByUserId.get(userId).addAll(
                    articlesByThemeIds.get(themeId) == null ?
                    new ArrayList<>()
                    :
                    articlesByThemeIds.get(themeId)
            );
        });

        articlesByUserId.forEach((userId, articles) ->
                emailService.sendArticles(
                        emailUserRepository.findByUserId(userId).getEmail(),
                        articles
                )
        );

        storage.clear();
    }
}