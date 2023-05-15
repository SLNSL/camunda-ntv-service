package ru.ntv.service;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ntv.dto.kafka.ArticleKafkaDTO;
import ru.ntv.dto.request.journalist.NewArticleRequest;
import ru.ntv.entity.articles.Theme;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.exception.ArticleNotFoundException;
import ru.ntv.dto.response.common.ArticlesResponse;
import ru.ntv.entity.articles.Article;
import ru.ntv.repo.article.ArticleRepository;
import ru.ntv.repo.article.ThemeRepository;
import ru.ntv.repo.user.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private final Producer<String, ArticleKafkaDTO> producer;

    public ArticleService(Producer<String, ArticleKafkaDTO> producer) {
        this.producer = producer;
    }
//    private final KafkaTemplate<String, ArticleKafkaDTO> template;

//    public ArticleService(KafkaTemplate<String, ArticleKafkaDTO>  template) {
//        this.template = template;
//    }

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

    @Transactional
    public Article createArticle(NewArticleRequest newArticleRequest) {

        Article article = convertNewArticleRequestToArticle(newArticleRequest);

        article = articleRepository.save(article);

        ArticleKafkaDTO articleKafkaDTO = new ArticleKafkaDTO();
        articleKafkaDTO.setHeader(newArticleRequest.getHeader());
        articleKafkaDTO.setSubheader(newArticleRequest.getSubheader());
        articleKafkaDTO.setText(newArticleRequest.getText());
        articleKafkaDTO.setPhotoURL(newArticleRequest.getPhotoURL());
        articleKafkaDTO.setThemes(
                article.getThemes().stream().map(Theme::getThemeName).collect(Collectors.toList())
        );

        article = articleRepository.findByHeader(articleKafkaDTO.getHeader()).get();

        ProducerRecord<String, ArticleKafkaDTO> record = new ProducerRecord<>(
                "article-topic",
                String.valueOf(article.getId()),
                articleKafkaDTO
        );

        log.info("New Producer record: " + record);
        producer.send(record, (recordMetadata, e) -> {
            log.info("Got ack from Kafka. Errors: " + e);
        });

        producer.close();



//        template.send("article-topic", articleKafkaDTO);


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



//    @Transactional
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        article.setJournalistName(userName);

        return article;
    }



    public List<Article> getArticlesByJournalistName(String name) {
        var journalist = userRepository.findByLogin(name).get(); //todo throw custom Exception if user is not found

        if (!Objects.equals(journalist.getRole().getRoleName(), DatabaseRole.ROLE_JOURNALIST.name())) throw new RuntimeException(); //todo throw custom Exception that isn't boss

        return articleRepository.findAllByJournalistName(journalist.getLogin());
    }
}