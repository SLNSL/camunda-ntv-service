package ru.ntv.delegates.articles;


import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ntv.entity.articles.Article;
import ru.ntv.entity.articles.Theme;
import ru.ntv.service.ArticleService;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
public class GetArticleById implements JavaDelegate {

    @Autowired
    private ArticleService articleService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        int id = Integer.parseInt((String) delegateExecution.getVariable("id"));
        Optional<Article> articleOptional = articleService.findById(id);
        if (articleOptional.isEmpty()) throw new BpmnError("Not found");
        Article article = articleOptional.get();
        delegateExecution.setVariable("header", article.getHeader());
        delegateExecution.setVariable("subheader", article.getSubheader());

        String themes = article.getThemes().stream()
                .map(Theme::getThemeName)
                .collect(Collectors.joining(", "));
        delegateExecution.setVariable("themes", themes);

        delegateExecution.setVariable("text", article.getText());
        delegateExecution.setVariable("photoUrl", article.getPhoto());

    }
}
