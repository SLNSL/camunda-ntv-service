package ru.ntv.delegates.articles;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntv.dto.request.journalist.NewArticleRequest;
import ru.ntv.entity.articles.Article;
import ru.ntv.service.ArticleService;

import java.util.Arrays;
import java.util.List;

@Component
public class PostArticle implements JavaDelegate {

    @Autowired
    private ArticleService articleService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String header = (String) delegateExecution.getVariable("Заголовок");
        String subheader = (String) delegateExecution.getVariable("Подзаголовок");
        String photoUrl = (String) delegateExecution.getVariable("PhotoUrl");
        Integer priority = Integer.parseInt((String) delegateExecution.getVariable("Приоритет"));
        String text = (String) delegateExecution.getVariable("Текст");
        String themeIdsString = (String) delegateExecution.getVariable("Темы");
        String journalistName = (String) delegateExecution.getVariable("username");
        List<Integer> themeIds = Arrays.stream(themeIdsString.split(", "))
                .map(Integer::parseInt)
                .toList();

        NewArticleRequest newArticleRequest = new NewArticleRequest(themeIds, header, subheader,
                text, priority, photoUrl,journalistName);

        Article article = articleService.createArticle(newArticleRequest);
        delegateExecution.setVariable("articleId", article.getId());
        delegateExecution.setVariable("articleObj", article);
    }
}